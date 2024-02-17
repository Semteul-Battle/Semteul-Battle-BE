package Winter_Project.Semteul_Battle.dto.Contest;

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
    private String language;
    private Long runtime;
    private String loginId;
    private Timestamp time;
    private Long result;
    private ProblemDTO problem;

}
