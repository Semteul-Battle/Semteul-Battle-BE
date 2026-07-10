// ContestApplicationController.java

package Winter_Project.Semteul_Battle.domain.contest.controller;

import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestApplicationDto;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestApplicationService;
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
            return ResponseEntity.ok(HttpStatus.OK+"??????????읐????????諛몃마???????????");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("??????????읐??????????ㅼ굣?????ル늉?? ????썹땟戮녹??醫딆맚嶺뚮㉡???????????????놁졄.");
        }
    }
}
