package Winter_Project.Semteul_Battle.domain.mail.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private String loginId;
    private String title;
    private String message;
    private String password;

    private String verificationCode;
}
