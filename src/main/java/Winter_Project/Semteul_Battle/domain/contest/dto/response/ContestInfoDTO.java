package Winter_Project.Semteul_Battle.domain.contest.dto.response;

import Winter_Project.Semteul_Battle.domain.contest.entity.ContestNotice;
import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContestInfoDTO {
    private Long id;
    private String number;
    private String title;
    private int score;
}
