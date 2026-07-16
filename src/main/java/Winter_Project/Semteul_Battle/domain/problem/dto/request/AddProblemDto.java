package Winter_Project.Semteul_Battle.domain.problem.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
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
public class AddProblemDto {
    @NotBlank(message = "문제 번호를 입력해주세요.")
    private String number;

    @NotBlank(message = "문제 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "문제 내용을 입력해주세요.")
    private String content;

    private String input;
    private String output;
    private String timeLimit;

    @PositiveOrZero(message = "점수는 0 이상이어야 합니다.")
    private int score = 0;

    private byte[] pic;

    @NotNull(message = "대회 ID를 입력해주세요.")
    private Long contestId;
}
