package Winter_Project.Semteul_Battle.domain.user.dto.response;

import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPageDto {
    private String userName;
    private String loginId;
    private String university;
    private String major;
    private String profile;
    private List<ContestInfoDto> contestList;

    public static UserPageDto from(Users user, List<ContestInfoDto> contestList) {
        return new UserPageDto(
                user.getName(),
                user.getLoginId(),
                user.getUniversity(),
                user.getMajor(),
                user.getProfile(),
                contestList
        );
    }
}
