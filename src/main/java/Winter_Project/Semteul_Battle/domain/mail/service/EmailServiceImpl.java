package Winter_Project.Semteul_Battle.domain.mail.service;

import Winter_Project.Semteul_Battle.domain.mail.dto.request.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SecureRandom secureRandom = new SecureRandom();
    private static final String FROM_EMAIL = "cham9561@naver.com";

    public MailDto certificationNumberIssued(String userEmail, String username) {
        String code = certificationNumber();
        return MailDto.verificationMail(
                userEmail,
                username + "님의 비밀번호 찾기 인증번호입니다.",
                username + "님의 비밀번호 찾기 인증번호는 [ " + code + " ] 입니다. 인증번호를 입력해 주세요.",
                code
        );
    }

    public MailDto signUpVerification(String userEmail) {
        String code = certificationNumber();
        return MailDto.verificationMail(
                userEmail,
                "회원가입 이메일 인증번호입니다.",
                "회원가입 인증번호는 [ " + code + " ] 입니다. 인증번호를 입력해 주세요.",
                code
        );
    }

    public String certificationNumber() {
        char[] source = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";

        int idx;
        for (int i = 0; i < 6; i++) {
            idx = secureRandom.nextInt(source.length);
            str += source[idx];
        }
        return str;
    }

    public void mailSend(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getEmail());
        message.setFrom(FROM_EMAIL);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
    }
}
