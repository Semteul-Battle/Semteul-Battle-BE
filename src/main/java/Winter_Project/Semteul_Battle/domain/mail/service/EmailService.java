package Winter_Project.Semteul_Battle.domain.mail.service;

import Winter_Project.Semteul_Battle.domain.mail.dto.request.MailDto;
import Winter_Project.Semteul_Battle.global.util.RedisUtil;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public interface EmailService {
    public MailDto certificationNumberIssued(String userEmail, String username);
    public MailDto signUpVerification(String userEmail);
    public String certificationNumber();
    public void mailSend(MailDto mailDto);
}
