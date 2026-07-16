package Winter_Project.Semteul_Battle.domain.user.dto.request;

import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SignUpDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "전공을 입력해주세요.")
    private String major;

    @NotBlank(message = "대학교를 입력해주세요.")
    private String university;

    public Users toEntity(String encodedPassword) {

        return Users.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .name(name)
                .email(email)
                .major(major)
                .university(university)
                .build();
    }
}
