package Winter_Project.Semteul_Battle.domain.problem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProblemDto {
    @NotNull(message = "문제 ID를 입력해주세요.")
    private Long problemId;

    private Long contestId;
    private Long ioId;
}
