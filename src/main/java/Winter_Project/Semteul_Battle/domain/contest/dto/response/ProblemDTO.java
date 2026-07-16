package Winter_Project.Semteul_Battle.domain.contest.dto.response;

import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemDTO {
    private Long id;
    private String number;

    public static ProblemDTO from(Problem problem) {
        return new ProblemDTO(problem.getId(), problem.getNumber());
    }
}
