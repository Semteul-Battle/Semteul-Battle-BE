package Winter_Project.Semteul_Battle.domain.contest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestQuestionDTO {
    private String token;
    private String question;
    private String content;
    private Timestamp questionTime;
    private Long userId;
    private Long contestId;
}