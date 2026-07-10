package Winter_Project.Semteul_Battle.domain.menu.dto.request;

import Winter_Project.Semteul_Battle.domain.menu.entity.MenuQuestion;
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
public class CommentDto {
    private String content;
    private Timestamp time;
    private Users users;
    private Long questionId;
}
