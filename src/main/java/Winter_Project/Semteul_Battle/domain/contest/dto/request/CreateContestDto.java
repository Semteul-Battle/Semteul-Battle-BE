package Winter_Project.Semteul_Battle.domain.contest.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CreateContestDto {

    @NotBlank(message = "대회 이름을 입력해주세요.")
    private String contestName;

    private List<String> examinerUsernames;

    @NotNull(message = "입장 권한을 입력해주세요.")
    private Long enterAuthority;

    @NotNull(message = "시작 시간을 입력해주세요.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp startTime;

    @NotNull(message = "종료 시간을 입력해주세요.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp endTime;

    private Long contestHost;
}
