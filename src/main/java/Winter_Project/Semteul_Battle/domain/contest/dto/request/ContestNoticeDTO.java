package Winter_Project.Semteul_Battle.domain.contest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContestNoticeDTO {
    @NotBlank(message = "공지 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "공지 내용을 입력해주세요.")
    private String content;

    @NotNull(message = "대회 ID를 입력해주세요.")
    private Long contestId;

    private Long userId;
}
