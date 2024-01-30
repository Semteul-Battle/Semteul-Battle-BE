package Winter_Project.Semteul_Battle.dto.Contest;

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

    private String contestName;
    private List<String> examinerUsernames;
    private Long enterAuthority;
    private Timestamp startTime;
    private Timestamp endTime;

}
