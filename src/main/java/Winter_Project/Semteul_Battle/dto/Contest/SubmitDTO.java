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
public class SubmitDTO {

    private Long id; //  문제 아이디(제출번호)
    private ProblemDTO problem; // 문제 아이디

    private String language; // 언어
    private Long runtime; // 실행 제한 시간
    private Long memoLimit; // 메모리 제한
    private Long userId; // 사용자 아이디
    private Timestamp time; // 제출 시간
    private Long result; // 결과
    private String code; // 코드

    private Long contestId; // 대회 번호

}
