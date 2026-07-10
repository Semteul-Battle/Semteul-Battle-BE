package Winter_Project.Semteul_Battle.domain.contest.dto.response;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.Examiner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContestPageDto {
    private List<Contest> contests;
    private List<Long> examinerIds;
    private List<Examiner> examiners;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private Integer prevPage;
    private Integer nextPage;
}
