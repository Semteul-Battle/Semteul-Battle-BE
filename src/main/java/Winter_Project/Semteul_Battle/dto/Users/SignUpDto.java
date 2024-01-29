package Winter_Project.Semteul_Battle.dto.Users;

import Winter_Project.Semteul_Battle.domain.Users;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

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
    private List<String> roles = new ArrayList<>();

    public Users toEntity(String encodedPassword, List<String> roles) {

        return Users.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .name(name)
                .email(email)
                .major(major)
                .university(university)
                .roles(roles)
                .build();
    }
}
