package Winter_Project.Semteul_Battle.domain.user.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SignInDto {
    private String loginId;
    private String password;
}
