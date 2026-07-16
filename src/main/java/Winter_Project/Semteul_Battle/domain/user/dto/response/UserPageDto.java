package Winter_Project.Semteul_Battle.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPageDto {
    private String userName;
    private String loginId;
    private String university;
    private String major;
    private String profile;
    private List<ContestInfoDto> contestList;
}
