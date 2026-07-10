package Winter_Project.Semteul_Battle.domain.mail.service;

import Winter_Project.Semteul_Battle.domain.mail.dto.request.MailDto;
import Winter_Project.Semteul_Battle.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private static final String FROM_EMAIL = "cham9561@naver.com";


public MailDto certificationNumberIssued(String userEmail, String username) {
        String code = certificationNumber();
        MailDto dto = new MailDto();
        dto.setEmail(userEmail);
        dto.setTitle(username + "님의 비밀번호 찾기 인증번호입니다.");
        dto.setMessage(username + "님의 비밀번호 찾기 인증번호는 [ " + code + " ] 입니다. 인증번호를 입력해 주세요.");
        dto.setVerificationCode(code);
        return dto;
    }


public MailDto signUpVerification(String userEmail) {
        String code = certificationNumber();
        MailDto dto = new MailDto();
        dto.setEmail(userEmail);
        dto.setTitle("회원가입 이메일 인증번호입니다.");
        dto.setMessage("회원가입 인증번호는 [ " + code + " ] 입니다. 인증번호를 입력해 주세요.");
        dto.setVerificationCode(code);

        System.out.println("debug");
        return dto;
    }


public String certificationNumber() {
        char[] Source = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";

        int idx = 0;
        for (int i = 0; i < 6; i++) {
            idx = (int) (Source.length * Math.random());
            str += Source[idx];
        }
        return str;
    }


public void mailSend(MailDto mailDto) {
        System.out.println("email send successful");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getEmail());
        message.setFrom(FROM_EMAIL);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
    }
}
