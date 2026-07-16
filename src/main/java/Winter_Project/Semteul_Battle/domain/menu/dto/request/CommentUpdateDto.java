package Winter_Project.Semteul_Battle.domain.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDto {
    @NotNull(message = "댓글 ID를 입력해주세요.")
    private Long commentId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    private Timestamp time;
}
