package Winter_Project.Semteul_Battle.domain.contest.dto.request;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
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
}
