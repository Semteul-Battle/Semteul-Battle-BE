package Winter_Project.Semteul_Battle.controller.Users;

import Winter_Project.Semteul_Battle.dto.MailDto;
import Winter_Project.Semteul_Battle.dto.Users.SignUpDto;
import Winter_Project.Semteul_Battle.dto.Users.UserDto;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import Winter_Project.Semteul_Battle.service.EmailService;
import Winter_Project.Semteul_Battle.service.Users.UserService;
import Winter_Project.Semteul_Battle.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class SignUpController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RedisUtil redisUtil;

    private Map<String, Boolean> pass = new HashMap<>();

    // id 중복체크 버튼
    @GetMapping("/id-check")
    public boolean idCheck(@RequestParam("loginId") String loginId) {
        // db에서 찾은 경우 중복
        if (userRepository.existsByLoginId(loginId)) {
            return false;
        }
        // db에 존재하지 않는 경우
        redisUtil.setDataExpire(loginId, "pass", 600);
        return true;
    }

    // 회원가입 인증 이메일 발송, front에 유저가 입력한 loginId요청
    @PostMapping("/send-email")
    public boolean signUpEmail(@RequestBody MailDto mailDto) {
        String email = mailDto.getEmail();

        if ("pass".equals(redisUtil.getData(mailDto.getLoginId()))) {
            MailDto signUpDto = emailService.signUpVerification(email);
            emailService.mailSend(signUpDto);
            redisUtil.setDataExpire(mailDto.getEmail(), signUpDto.getVerificationCode(), 300);
            return true;
        } else {
            return false;
        }
    }

    // 인증 절차
    @PostMapping("/verification")
    public boolean verifyCode(@RequestBody MailDto mailDto) {

        String storedValue = redisUtil.getData(mailDto.getEmail());
        String UserCode = mailDto.getVerificationCode();
        if (storedValue != null && storedValue.equals(UserCode)) { // 인증 성공
            redisUtil.setDataExpire(mailDto.getEmail(), "pass", 600);
            return true;
        } else { // 인증 실패
            return false;
        }
    }

    // 회원가입 버튼
    @PostMapping("/sign-up")
    public boolean signUp(@RequestBody SignUpDto signUpDto) {
        if (signUpDto.getLoginId() != null && signUpDto.getPassword() != null && signUpDto.getName() != null && signUpDto.getEmail() != null && signUpDto.getMajor() != null && "pass".equals(redisUtil.getData(signUpDto.getEmail()))) {
            UserDto savedUserDto = userService.signUp(signUpDto);
            return true;
        }
        return false;
    }
}