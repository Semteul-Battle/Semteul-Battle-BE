package Winter_Project.Semteul_Battle.domain.contest.dto.request;

import Winter_Project.Semteul_Battle.domain.contest.dto.response.ProblemDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitDTO {

    private Long id;
    private ProblemDTO problem;

    @NotBlank(message = "언어를 입력해주세요.")
    private String language;

    private Long runtime;
    private Long memoLimit;
    private Long userId;
    private Timestamp time;
    private Long result;

    @NotBlank(message = "제출 코드를 입력해주세요.")
    private String code;

    @NotNull(message = "대회 ID를 입력해주세요.")
    private Long contestId;
}
