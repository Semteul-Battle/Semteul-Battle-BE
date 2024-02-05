package Winter_Project.Semteul_Battle.dto.ContestPage;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Page {
    // 최소 페이지 번호
    private int min;
    // 최대 페이지 번호
    private int max;
    // 이전 버튼의 페이지 번호
    private int prevPage;
    // 현재 페이지 번호
    private int currentPage;
    // 다음 버튼의 페이지 번호
    private int nextPage;
    // 전체 페이지 개수
    private int pageCnt;

}
