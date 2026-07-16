package Winter_Project.Semteul_Battle.domain.menu.dto.request;

import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
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
public class QuestionDto {
    @NotBlank(message = "질문 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "질문 내용을 입력해주세요.")
    private String content;

    private Timestamp time;
    private Users users;

    public void recordWrittenAt(Timestamp time) {
        this.time = time;
    }
}
