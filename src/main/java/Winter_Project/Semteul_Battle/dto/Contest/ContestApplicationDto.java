package Winter_Project.Semteul_Battle.dto.Contest;

import Winter_Project.Semteul_Battle.domain.Contest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContestApplicationDto {
    private String contestName;
    private String loginId;
    private String accessToken;
}