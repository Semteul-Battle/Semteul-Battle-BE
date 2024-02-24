package Winter_Project.Semteul_Battle.dto.MenuNotice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoticeUpdateDto {
    private Long noticeId;
    private Timestamp time;
    private String title;
    private String content;
}
