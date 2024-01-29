package Winter_Project.Semteul_Battle.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {
    private String email;
    private String loginId;
    private String title;
    private String message;
    private String password;
    private String verificationCode;
}
