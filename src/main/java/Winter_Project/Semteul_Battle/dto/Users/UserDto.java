package Winter_Project.Semteul_Battle.dto.Users;

import Winter_Project.Semteul_Battle.domain.Users;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
public class UserDto {
    private String name;
    private String loginId;
    private String password;
    private String university;
    private String major;
    private String email;
    private int authority;
    private String profile;
    private int view;

    static public UserDto toDto(Users users) {
        return UserDto.builder()
                .loginId(users.getLoginId())
                .name(users.getName())
                .password(users.getPassword())
                .email(users.getEmail())
                .major(users.getMajor())
                .university(users.getUniversity())
                .authority(users.getAuthority())
                .profile(users.getProfile())
                .view(users.getView())
                .build();
    }

    public Users toEntity() {
        return Users.builder()
                .loginId(loginId)
                .name(name)
                .password(password)
                .email(email)
                .major(major)
                .university(university)
                .authority(authority)
                .profile(profile)
                .view(view)
                .build();
    }
}
