package Winter_Project.Semteul_Battle.dto.MenuComment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDto {
    private Long commentId;
    private String content;
    private Timestamp time;
}
