package Winter_Project.Semteul_Battle.domain.problem.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddIODto {
    @NotNull(message = "문제 ID를 입력해주세요.")
    private Long problemId;

    @NotNull(message = "대회 ID를 입력해주세요.")
    private Long contestId;

    private String input;
    private String output;
}
