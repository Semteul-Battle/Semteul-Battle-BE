package Winter_Project.Semteul_Battle.domain.menu.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CommentUpdateDto {
    @NotNull(message = "댓글 ID를 입력해주세요.")
    private Long commentId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    private Timestamp time;

    public void recordUpdatedAt(Timestamp time) {
        this.time = time;
    }
}
