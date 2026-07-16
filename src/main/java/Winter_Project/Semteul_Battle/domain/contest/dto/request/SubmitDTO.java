package Winter_Project.Semteul_Battle.domain.contest.dto.request;

import Winter_Project.Semteul_Battle.domain.contest.dto.response.ProblemDTO;
import Winter_Project.Semteul_Battle.domain.contest.entity.Submit;
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
public class SubmitDTO {

    private Long id;
    private ProblemDTO problem;

    @NotNull(message = "문제 ID를 입력해주세요.")
    private Long problemId;

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

    public static SubmitDTO from(Submit submit) {
        SubmitDTO dto = new SubmitDTO();
        dto.problem = ProblemDTO.from(submit.getProblem());
        dto.problemId = submit.getProblem().getId();
        dto.language = submit.getLanguage();
        dto.runtime = submit.getRuntime();
        dto.time = submit.getTime();
        dto.result = submit.getResult();
        dto.userId = submit.getUsers().getId();
        return dto;
    }
}
