package Winter_Project.Semteul_Battle.domain.user.controller;

import Winter_Project.Semteul_Battle.domain.mail.dto.request.MailDto;
import Winter_Project.Semteul_Battle.domain.mail.service.EmailService;
import Winter_Project.Semteul_Battle.domain.user.dto.request.SignUpDto;
import Winter_Project.Semteul_Battle.domain.user.exception.UserException;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.domain.user.service.UserService;
import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import Winter_Project.Semteul_Battle.global.util.RedisUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class SignUpController {

    private static final String VERIFIED = "pass";
    private static final String SIGNUP_ID_KEY_PREFIX = "signup:id:";
    private static final String SIGNUP_EMAIL_KEY_PREFIX = "signup:email:";
    private static final long SIGN_UP_STEP_TTL_SECONDS = 300L;

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RedisUtil redisUtil;

    @GetMapping("/id-check")
    public BaseResponse<Boolean> idCheck(@RequestParam("loginId") String loginId) {
        requireText(loginId, "아이디를 입력해주세요.");

        boolean available = !userRepository.existsByLoginId(loginId);
        if (available) {
            redisUtil.setDataExpire(signUpIdKey(loginId), VERIFIED, SIGN_UP_STEP_TTL_SECONDS);
        }
        return BaseResponse.onSuccess(SuccessStatus.OK, available);
    }

    @PostMapping("/send-email")
    public BaseResponse<Void> signUpEmail(@RequestBody @Valid MailDto mailDto) {
        requireMailFields(mailDto, true, false);

        if (!VERIFIED.equals(redisUtil.getData(signUpIdKey(mailDto.getLoginId())))) {
            throw new UserException(ErrorStatus._BAD_REQUEST, "아이디 중복 확인이 필요합니다.");
        }

        MailDto signUpDto = emailService.signUpVerification(mailDto.getEmail());
        emailService.mailSend(signUpDto);
        redisUtil.setDataExpire(signUpEmailKey(mailDto.getEmail()), signUpDto.getVerificationCode(), SIGN_UP_STEP_TTL_SECONDS);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @PostMapping("/verification")
    public BaseResponse<Void> verifyCode(@RequestBody @Valid MailDto mailDto) {
        requireMailFields(mailDto, false, true);

        String storedValue = redisUtil.getData(signUpEmailKey(mailDto.getEmail()));
        if (storedValue == null || !storedValue.equals(mailDto.getVerificationCode())) {
            throw new UserException(ErrorStatus._BAD_REQUEST, "이메일 인증 코드가 일치하지 않습니다.");
        }

        redisUtil.setDataExpire(signUpEmailKey(mailDto.getEmail()), VERIFIED, SIGN_UP_STEP_TTL_SECONDS);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @PostMapping("/sign-up")
    public BaseResponse<Void> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        if (signUpDto == null || !StringUtils.hasText(signUpDto.getEmail())) {
            throw new UserException(ErrorStatus._BAD_REQUEST, "회원가입 필수 정보를 입력해주세요.");
        }

        if (!VERIFIED.equals(redisUtil.getData(signUpEmailKey(signUpDto.getEmail())))) {
            throw new UserException(ErrorStatus._BAD_REQUEST, "이메일 인증이 필요합니다.");
        }

        userService.signUp(signUpDto);
        redisUtil.deleteData(signUpIdKey(signUpDto.getLoginId()));
        redisUtil.deleteData(signUpEmailKey(signUpDto.getEmail()));
        return BaseResponse.onSuccess(SuccessStatus.CREATED, null);
    }

    private void requireMailFields(MailDto mailDto, boolean loginIdRequired, boolean codeRequired) {
        if (mailDto == null || !StringUtils.hasText(mailDto.getEmail())) {
            throw new UserException(ErrorStatus._BAD_REQUEST, "이메일을 입력해주세요.");
        }
        if (loginIdRequired) {
            requireText(mailDto.getLoginId(), "아이디를 입력해주세요.");
        }
        if (codeRequired) {
            requireText(mailDto.getVerificationCode(), "인증 코드를 입력해주세요.");
        }
    }

    private void requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new UserException(ErrorStatus._BAD_REQUEST, message);
        }
    }

    private String signUpIdKey(String loginId) {
        return SIGNUP_ID_KEY_PREFIX + loginId;
    }

    private String signUpEmailKey(String email) {
        return SIGNUP_EMAIL_KEY_PREFIX + email;
    }
}
