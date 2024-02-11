package Winter_Project.Semteul_Battle.controller.Problem;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.dto.Problem.AddProblemDto;
import Winter_Project.Semteul_Battle.service.Contest.ContestService;
import Winter_Project.Semteul_Battle.service.Problem.AddProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contests")
public class AddProblemController {
    private final AddProblemService addProblemService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ContestService contestService;

    @PostMapping("/add")
    public boolean addProblem(@RequestBody AddProblemDto addProblemDto,
                              @RequestHeader("Authorization") String token) {
        Long contestId = addProblemDto.getContestId();
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        if (participantStatus == 0) { // 출제자가 맞는 경우
            addProblemService.problemFrame(addProblemDto);
            return true;
        } else { // 출제자가 아닌 경우
            return false;
        }
    }

//    @DeleteMapping("/delete")
//    public boolean deleteProblem()
}
