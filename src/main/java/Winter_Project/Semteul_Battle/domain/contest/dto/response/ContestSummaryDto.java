package Winter_Project.Semteul_Battle.domain.contest.dto.response;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class ContestSummaryDto {
    private Long id;
    private String contestName;
    private Long enterAuthority;
    private Timestamp startTime;
    private Timestamp endTime;
    private String simpleInfo;
    private String problemInfo;
    private Long contestHost;

    public static ContestSummaryDto from(Contest contest) {
        return ContestSummaryDto.builder()
                .id(contest.getId())
                .contestName(contest.getContestName())
                .enterAuthority(contest.getEnterAuthority())
                .startTime(contest.getStartTime())
                .endTime(contest.getEndTime())
                .simpleInfo(contest.getSimpleInfo())
                .problemInfo(contest.getProblemInfo())
                .contestHost(contest.getContestHost())
                .build();
    }
}
