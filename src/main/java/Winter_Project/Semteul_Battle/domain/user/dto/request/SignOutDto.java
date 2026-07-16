package Winter_Project.Semteul_Battle.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SignOutDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;
}
