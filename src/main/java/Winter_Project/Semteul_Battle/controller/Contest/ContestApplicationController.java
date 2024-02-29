// ContestApplicationController.java

package Winter_Project.Semteul_Battle.controller.Contest;

import Winter_Project.Semteul_Battle.dto.Contest.ContestApplicationDto;
import Winter_Project.Semteul_Battle.service.Contest.ContestApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contests")
public class ContestApplicationController {
    private final ContestApplicationService contestApplicationService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyContest(@RequestBody ContestApplicationDto contestApplicationDto,
                                               @RequestHeader("Authorization") String token) {
        boolean result = contestApplicationService.applyContest(contestApplicationDto, token);
        if (result) {
            return ResponseEntity.ok(HttpStatus.OK+"대회 신청이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("대회 신청 중 오류가 발생했습니다.");
        }
    }
}
