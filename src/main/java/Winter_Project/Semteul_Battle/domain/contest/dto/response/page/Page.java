package Winter_Project.Semteul_Battle.domain.contest.dto.response.page;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Page {

private int min;

private int max;

private int prevPage;

private int currentPage;

private int nextPage;

private int pageCnt;

}
