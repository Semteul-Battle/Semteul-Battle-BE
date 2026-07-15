package Winter_Project.Semteul_Battle.domain.user.dto.request;

import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SignUpDto {
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String major;
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
