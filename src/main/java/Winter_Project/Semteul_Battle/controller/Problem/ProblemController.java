package Winter_Project.Semteul_Battle.controller.Problem;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.dto.Problem.AddProblemDto;
import Winter_Project.Semteul_Battle.dto.Problem.DeleteProblemDto;
import Winter_Project.Semteul_Battle.repository.ContestRepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import Winter_Project.Semteul_Battle.service.Contest.ContestLiveService;
import Winter_Project.Semteul_Battle.service.Contest.ContestService;
import Winter_Project.Semteul_Battle.service.Problem.AddProblemService;
import Winter_Project.Semteul_Battle.service.Problem.DeleteProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contests")
public class ProblemController {
    private final AddProblemService addProblemService;
    private final JwtTokenProvider  jwtTokenProvider;
    private final ContestService contestService;
    private final DeleteProblemService deleteProblemService;
    private final ProblemRepository problemRepository;
    private final ContestRepository contestRepository;
    private final ContestLiveService contestLiveService;

    @PostMapping("/add")
    public boolean addProblem(@RequestBody AddProblemDto addProblemDto,
                              @RequestHeader("Authorization") String token) {
        Long contestId = addProblemDto.getContestId();
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestLiveService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        if (participantStatus == 0) { // 출제자가 맞는 경우
            addProblemService.problemFrame(addProblemDto);
            return true;
        } else { // 출제자가 아닌 경우
            return false;
        }
    }

    @DeleteMapping("/delete")
    public boolean deleteProblem(@RequestBody DeleteProblemDto deleteProblemDto,
                                 @RequestHeader("Authorization") String token) {
        Long contestId = deleteProblemDto.getContestId();
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestLiveService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        // 출제자 여부 확인
        if (participantStatus == 0) {
            // 해당 대회와 문제가 존재하는지 확인
            boolean contestExists = contestRepository.existsById(contestId);
            boolean problemExists = problemRepository.existsById(deleteProblemDto.getProblemId());

            // 대회와 문제가 모두 존재하는 경우에만 문제 삭제 서비스 호출
            if (contestExists && problemExists) {
                deleteProblemService.deleteProblem(deleteProblemDto);
                return true;
            } else {
                // 대회 또는 문제가 존재하지 않는 경우
                return false;
            }
        } else { // 출제자가 아닌 경우
            return false;
        }
    }
}