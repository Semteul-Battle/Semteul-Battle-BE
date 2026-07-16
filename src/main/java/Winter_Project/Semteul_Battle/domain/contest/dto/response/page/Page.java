package Winter_Project.Semteul_Battle.domain.contest.dto.response.page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Page {

private int min;

private int max;

private int prevPage;

private int currentPage;

private int nextPage;

private int pageCnt;

}
