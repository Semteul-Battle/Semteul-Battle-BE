package Winter_Project.Semteul_Battle.domain.contest.dto.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ContestQuestionDTO {
    private String token;

    @NotBlank(message = "질문 제목을 입력해주세요.")
    private String question;

    @NotBlank(message = "질문 내용을 입력해주세요.")
    private String content;

    private Timestamp questionTime;
    private Long userId;

    @NotNull(message = "대회 ID를 입력해주세요.")
    private Long contestId;

    public void assignUserId(Long userId) {
        this.userId = userId;
    }
}
