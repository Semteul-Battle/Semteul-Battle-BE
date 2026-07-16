package Winter_Project.Semteul_Battle.domain.menu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeleteDto {
    @NotNull(message = "댓글 ID를 입력해주세요.")
    private Long CommentId;
}
