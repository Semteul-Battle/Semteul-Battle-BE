package Winter_Project.Semteul_Battle.dto.Contest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemDTO {
    private Long id;
    private String number;
}