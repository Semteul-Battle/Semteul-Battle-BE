package Winter_Project.Semteul_Battle.domain.menu.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticePageDto {
    private List<NoticeCheckDto> notices;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private Integer prevPage;
    private Integer nextPage;
}
