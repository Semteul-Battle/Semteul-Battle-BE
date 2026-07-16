package Winter_Project.Semteul_Battle.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContestInfoDto {
    private String contestName;
    private Long enterAuthority;

    public static ContestInfoDto of(String contestName, Long enterAuthority) {
        return new ContestInfoDto(contestName, enterAuthority);
    }
}
