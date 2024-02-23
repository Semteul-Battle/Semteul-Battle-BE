package Winter_Project.Semteul_Battle.dto.Problem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddProblemDto {
    private String number;
    private String title;
    private String content;
    private String input;
    private String output;
    private String timeLimit;
    private int score = 0;
    private byte[] pic;
    private Long contestId;
}
