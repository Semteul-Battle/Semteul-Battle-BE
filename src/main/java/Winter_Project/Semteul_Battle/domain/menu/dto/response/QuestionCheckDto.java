package Winter_Project.Semteul_Battle.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCheckDto {
    private String loginId;
    private String title;
    private String content;
    private Timestamp time;
}
