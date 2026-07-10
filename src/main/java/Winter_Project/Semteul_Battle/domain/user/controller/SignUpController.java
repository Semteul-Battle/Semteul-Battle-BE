package Winter_Project.Semteul_Battle.domain.user.controller;

import Winter_Project.Semteul_Battle.domain.mail.dto.request.MailDto;
import Winter_Project.Semteul_Battle.domain.user.dto.request.SignUpDto;
import Winter_Project.Semteul_Battle.domain.user.dto.response.UserDto;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.domain.mail.service.EmailService;
import Winter_Project.Semteul_Battle.domain.user.service.UserService;
import Winter_Project.Semteul_Battle.global.util.RedisUtil;
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

    // id ?關???꾨き??熬곥룊????????袁ㅻ쇀???癲?????
@GetMapping("/id-check")
    public boolean idCheck(@RequestParam("loginId") String loginId) {
        // db??????饔낅떽?????? ??棺堉?뤃?????關???꾨き??熬곥룊???
if (userRepository.existsByLoginId(loginId)) {
return false;
        }
        // db?????怨쀫뮡?????? ??????猿딅빟 ??棺堉?뤃????        redisUtil.setDataExpire(loginId, "pass", 600);
return true;
    }

    // ?????????????쎛????轅붽틓?????믊븐꽫???????????썹땟戮녹??醫딆맚?? front?????????ル늉?? ??????ㅼ굣塋??loginId????嫄???
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

    // ??轅붽틓?????믊븐꽫???????
@PostMapping("/verification")
    public boolean verifyCode(@RequestBody MailDto mailDto) {

        String storedValue = redisUtil.getData(mailDto.getEmail());
        String UserCode = mailDto.getVerificationCode();
        if (storedValue != null && storedValue.equals(UserCode)) { // ??轅붽틓?????믊븐꽫???關?쒎첎????곌램伊??            redisUtil.setDataExpire(mailDto.getEmail(), "pass", 600);
            return true;
        } else { // ??轅붽틓?????믊븐꽫???????⑤슣??
return false;
        }
    }

    // ?????????????쎛???癲?????
@PostMapping("/sign-up")
    public boolean signUp(@RequestBody SignUpDto signUpDto) {
        if (signUpDto.getLoginId() != null && signUpDto.getPassword() != null && signUpDto.getName() != null && signUpDto.getEmail() != null && signUpDto.getMajor() != null && "pass".equals(redisUtil.getData(signUpDto.getEmail()))) {
            UserDto savedUserDto = userService.signUp(signUpDto);
            return true;
        }
        return false;
    }
}