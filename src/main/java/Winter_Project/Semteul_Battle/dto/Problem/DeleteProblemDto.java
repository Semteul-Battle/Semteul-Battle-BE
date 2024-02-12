package Winter_Project.Semteul_Battle.dto.Problem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProblemDto {
    private Long problemId;
    private Long contestId;
}
