package Winter_Project.Semteul_Battle.dto.Problem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddIOFileDto {
    private Long contestId;
    private Long problemId;
    private String number;
    private String inputFile;
    private String outputFile;
}
