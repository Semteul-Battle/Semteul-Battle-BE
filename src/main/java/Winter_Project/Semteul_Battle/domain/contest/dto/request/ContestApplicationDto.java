package Winter_Project.Semteul_Battle.domain.contest.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class ContestApplicationDto {
    @NotBlank(message = "대회 이름을 입력해주세요.")
    private String contestName;
}
