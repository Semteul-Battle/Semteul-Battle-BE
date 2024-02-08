// ContestApplicationController.java

package Winter_Project.Semteul_Battle.controller.Contest;

import Winter_Project.Semteul_Battle.dto.Contest.ContestApplicationDto;
import Winter_Project.Semteul_Battle.service.Contest.ContestApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contests")
public class ContestApplicationController {
    private final ContestApplicationService contestApplicationService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyContest(@RequestBody ContestApplicationDto contestApplicationDto) {
        boolean result = contestApplicationService.applyContest(contestApplicationDto);
        if (result) {
            return ResponseEntity.ok("대회 신청이 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("대회 신청 중 오류가 발생했습니다.");
        }
    }
}
