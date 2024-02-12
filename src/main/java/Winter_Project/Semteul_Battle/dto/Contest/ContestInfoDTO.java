package Winter_Project.Semteul_Battle.dto.Contest;

import Winter_Project.Semteul_Battle.domain.ContestNotice;
import Winter_Project.Semteul_Battle.domain.Problem;
import lombok.*;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContestInfoDTO {
    private Long id;
    private String number;
    private String title;
    private int score;
}
