package Winter_Project.Semteul_Battle.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
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
