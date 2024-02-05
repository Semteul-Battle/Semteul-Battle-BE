package Winter_Project.Semteul_Battle.controller.Users;

import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.MailDto;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import Winter_Project.Semteul_Battle.service.Users.CustomUserDetailsService;
import Winter_Project.Semteul_Battle.service.Email.EmailService;
import Winter_Project.Semteul_Battle.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class FindPasswordController {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RedisUtil redisUtil;

    // 비밀번호 찾기 이메일 발송
    @PostMapping("/send")
    public boolean sendEmail(@RequestBody MailDto mailDto) {
        String email = mailDto.getEmail();
        String loginId = mailDto.getLoginId();

        // 아이디, 이메일 존재 유무 확인
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
        Optional<Users> existingUser = userRepository.findByEmail(email);

        if (userDetails != null && existingUser.isPresent()) {
            // 인증번호 발급
            MailDto emailDto = emailService.certificationNumberIssued(email, loginId);

            // 이메일 발송
            emailService.mailSend(emailDto);

            // 발급한 인증번호 저장
            redisUtil.setDataExpire(emailDto.getEmail(), emailDto.getVerificationCode(), 60 * 3L);

            return true;
        }
        // 아이디나 이메일이 존재하지 않음
        return false;
    }

    // 인증번호를 통한 인증 절차
    // 프론트한테 email요청
    @PostMapping("/find")
    public boolean verifyCode(@RequestBody MailDto mailDto) {

        String storedValue = redisUtil.getData(mailDto.getEmail());
        String UserCode = mailDto.getVerificationCode();
        if (storedValue != null && storedValue.equals(UserCode)) { // 인증 성공
            return true;
        } else { // 인증 실패
            return false;
        }
    }

    // 기존의 비밀번호를 초기화하고 새로운 비밀번호로 수정
    @PutMapping("/update")
    public boolean passwordUpdate(@RequestBody MailDto mailDto) {
        String newPassword = mailDto.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Optional<Users> usersOptional = userRepository.findByLoginId(mailDto.getLoginId());

        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();

            // 새로운 비밀번호 암호화
            String encodedPassword = encoder.encode(newPassword);

            // 비밀번호 업데이트
            users.setPassword(encodedPassword);

            // 변경된 상태 저장
            userRepository.save(users);
            return true;
        } else {
            return false;
        }
    }
}
