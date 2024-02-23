package Winter_Project.Semteul_Battle.dto.MenuQuestion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCheckDto {
    private String loginId; // 사용자의 loginId
    private String title;
    private String content;
    private Timestamp time;
}
