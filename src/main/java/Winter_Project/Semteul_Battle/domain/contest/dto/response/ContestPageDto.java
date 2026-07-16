package Winter_Project.Semteul_Battle.domain.contest.dto.response;

import Winter_Project.Semteul_Battle.domain.contest.entity.Examiner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContestPageDto {
    private List<ContestSummaryDto> contests;
    private List<Long> examinerIds;
    private List<Examiner> examiners;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private Integer prevPage;
    private Integer nextPage;
}
