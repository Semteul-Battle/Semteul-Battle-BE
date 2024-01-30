package Winter_Project.Semteul_Battle.service;

import Winter_Project.Semteul_Battle.dto.MailDto;
import Winter_Project.Semteul_Battle.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private static final String FROM_EMAIL = "cham9561@naver.com";

    // 비밀번호 찾기 이메일 작성
    public MailDto certificationNumberIssued(String userEmail, String username) {
        String code = certificationNumber();
        MailDto dto = new MailDto();
        dto.setEmail(userEmail);
        dto.setTitle(username + "님의 비밀번호 찾기 인증번호 발급 안내드립니다.");
        dto.setMessage("안녕하십니까? 비밀번호 찾기 인증번호를 안내드립니다. " + username + "님의 인증번호는 " + "[ " + code + " ]" + "입니다.\n"
                + "인증 후 비밀번호를 변경해주세요.");
        dto.setVerificationCode(code);
        return dto;
    }

    // 회원가입 인증 이메일 작성
    public MailDto signUpVerification(String userEmail) {
        String code = certificationNumber();
        MailDto dto = new MailDto();
        dto.setEmail(userEmail);
        dto.setTitle("회원가입 인증번호 발급 안내드립니다.\n");
        dto.setMessage("안녕하십니까? 회원가입 인증번호를 안내드립니다.\n"
                + "인증번호는 " + "[ " + code + " ]" + "입니다.\n"
                + "인증번호 인증 후 회원가입을 완료하시면 됩니다.");
        dto.setVerificationCode(code);

        System.out.println("회원가입 토큰" + code);
        return dto;
    }

    // 인증번호 생성
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

    // 이메일 발행
    public void mailSend(MailDto mailDto) {
        System.out.println("email send successful");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getEmail());
        message.setFrom(EmailService.FROM_EMAIL);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
    }
}
