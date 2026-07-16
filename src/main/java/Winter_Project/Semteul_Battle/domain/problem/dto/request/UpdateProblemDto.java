package Winter_Project.Semteul_Battle.domain.problem.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UpdateProblemDto {
    private String title;
    private String content;
    private String input;
    private String output;
    private String inputIO;
    private String outputIO;
    private String timeLimit;

    @PositiveOrZero(message = "점수는 0 이상이어야 합니다.")
    private int score = 0;

    private byte[] pic;
    private Long contestId;

    @NotNull(message = "문제 ID를 입력해주세요.")
    private Long problemId;

    private Long ioId;
}
