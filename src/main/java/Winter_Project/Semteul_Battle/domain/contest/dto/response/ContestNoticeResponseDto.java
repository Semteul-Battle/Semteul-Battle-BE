package Winter_Project.Semteul_Battle.domain.contest.dto.response;

import Winter_Project.Semteul_Battle.domain.contest.entity.ContestNotice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class ContestNoticeResponseDto {
    private Long id;
    private String title;
    private String content;
    private Timestamp time;
    private Long contestId;
    private Long userId;
    private String userName;

    public static ContestNoticeResponseDto from(ContestNotice notice) {
        return ContestNoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .time(notice.getTime())
                .contestId(notice.getContest().getId())
                .userId(notice.getUsers().getId())
                .userName(notice.getUsers().getName())
                .build();
    }
}
