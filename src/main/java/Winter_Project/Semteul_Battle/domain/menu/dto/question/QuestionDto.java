package Winter_Project.Semteul_Battle.domain.menu.dto.question;

import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private String title;
    private String content;
    private Timestamp time;
    private Users users;
}
