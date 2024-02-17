package Winter_Project.Semteul_Battle.dto.Contest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private Long questionId;
    private String answer; // 질문 대답
    private Timestamp answerTime; // 답변 작성 시간
    private Long answerer;

}
