package Winter_Project.Semteul_Battle.dto.UserPage;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Base64;
import java.util.List;

@Data
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