package Winter_Project.Semteul_Battle.domain.user.controller;

import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.mail.dto.request.MailDto;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.domain.user.service.CustomUserDetailsService;
import Winter_Project.Semteul_Battle.domain.mail.service.EmailService;
import Winter_Project.Semteul_Battle.global.util.RedisUtil;
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


@PostMapping("/send")
    public boolean sendEmail(@RequestBody MailDto mailDto) {
        String email = mailDto.getEmail();
        String loginId = mailDto.getLoginId();


UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
        Optional<Users> existingUser = userRepository.findByEmail(email);

        if (userDetails != null && existingUser.isPresent()) {





return true;
        }

return false;
    }


@PostMapping("/find")
    public boolean verifyCode(@RequestBody MailDto mailDto) {

        String storedValue = redisUtil.getData(mailDto.getEmail());
        String UserCode = mailDto.getVerificationCode();
        if (storedValue != null && storedValue.equals(UserCode)) {
return true;
        } else {
return false;
        }
    }


@PutMapping("/update")
    public boolean passwordUpdate(@RequestBody MailDto mailDto) {
        String newPassword = mailDto.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Optional<Users> usersOptional = userRepository.findByLoginId(mailDto.getLoginId());

        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();


String encodedPassword = encoder.encode(newPassword);




return true;
        } else {
            return false;
        }
    }
}
