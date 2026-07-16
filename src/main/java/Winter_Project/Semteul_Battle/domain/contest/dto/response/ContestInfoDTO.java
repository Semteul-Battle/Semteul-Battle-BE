package Winter_Project.Semteul_Battle.domain.contest.dto.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContestInfoDTO {
    private Long id;
    private String number;
    private String title;
    private int score;
}
