package Winter_Project.Semteul_Battle.domain.contest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    @NotNull(message = "질문 ID를 입력해주세요.")
    private Long questionId;

    @NotBlank(message = "답변 내용을 입력해주세요.")
    private String answer;

    private Timestamp answerTime;
    private Long answerer;
}
