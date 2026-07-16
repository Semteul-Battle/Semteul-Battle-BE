package Winter_Project.Semteul_Battle.domain.mail.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MailDto {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    private String loginId;
    private String title;
    private String message;
    private String password;
    private String verificationCode;

    public static MailDto verificationMail(String email, String title, String message, String verificationCode) {
        MailDto dto = new MailDto();
        dto.email = email;
        dto.title = title;
        dto.message = message;
        dto.verificationCode = verificationCode;
        return dto;
    }
}
