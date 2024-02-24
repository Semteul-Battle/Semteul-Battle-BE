package Winter_Project.Semteul_Battle.dto.MenuComment;

import Winter_Project.Semteul_Battle.domain.MenuQuestion;
import Winter_Project.Semteul_Battle.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String content;
    private Timestamp time;
    private Users users;
    private Long questionId;
}
