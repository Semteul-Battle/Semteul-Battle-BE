package Winter_Project.Semteul_Battle.domain.menu.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateDto {
    private Long questionId;
    private Timestamp time;
    private String title;
    private String content;
}
