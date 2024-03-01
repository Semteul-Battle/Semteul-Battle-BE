package Winter_Project.Semteul_Battle.dto.Contest;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Examiner;
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