package Winter_Project.Semteul_Battle.domain.contest.dto.request;

import Winter_Project.Semteul_Battle.domain.contest.dto.response.ProblemDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitDTO {

    private Long id;
    private ProblemDTO problem;
    private String language;
    private Long runtime;
    private Long memoLimit;
    private Long userId;
    private Timestamp time;
    private Long result;
    private String code;
    private Long contestId;
}
