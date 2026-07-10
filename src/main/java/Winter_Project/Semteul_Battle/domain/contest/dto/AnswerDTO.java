package Winter_Project.Semteul_Battle.domain.contest.dto;

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
    private String answer;
    private Timestamp answerTime;
    private Long answerer;
}
