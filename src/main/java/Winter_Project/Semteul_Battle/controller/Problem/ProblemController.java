package Winter_Project.Semteul_Battle.controller.Problem;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.dto.Problem.*;
import Winter_Project.Semteul_Battle.repository.ContestRepository;
import Winter_Project.Semteul_Battle.repository.IORepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import Winter_Project.Semteul_Battle.service.Contest.ContestLiveService;
import Winter_Project.Semteul_Battle.service.Problem.*;
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
    private final DeleteProblemService deleteProblemService;
    private final DeleteIOService deleteIOService;
    private final ProblemRepository problemRepository;
    private final ContestRepository contestRepository;
    private final ContestLiveService contestLiveService;
    private final UpdateProblemService updateProblemService;
    private final UpdateIOService updateIOService;
    private final IORepository ioRepository;
    private final AddIOService addIOService;

    // 공백 문제 추가
    @PostMapping("/addProblem")
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
    //공백 예시 추가
    @PostMapping("/addIO")
    public boolean addIO(@RequestBody AddIODto addIODto,
                         @RequestHeader("Authorization") String token) {
        Long contestId = addIODto.getContestId();
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestLiveService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        if (participantStatus == 0) { // 출제자가 맞는 경우
            addIOService.IOFrame(addIODto);
            return true;
        } else { // 출제자가 아닌 경우
            return false;
        }
    }


    @DeleteMapping("/deleteProblem")
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
            boolean ioExists = ioRepository.existsById(deleteProblemDto.getIoId());
            // 대회와 문제가 모두 존재하는 경우에만 문제 삭제 서비스 호출
            if (contestExists && problemExists && ioExists) {
                deleteIOService.deleteIO(deleteProblemDto);
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

    @PatchMapping("/updateProblem")
    public boolean updateProblem(@RequestBody UpdateProblemDto updateProblemDto,
                                 @RequestHeader("Authorization") String token) {
        Long contestId = updateProblemDto.getContestId();
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestLiveService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        // 출제자 여부 확인
        if (participantStatus == 0) {
            // 해당 대회와 문제가 존재하는지 확인
            boolean contestExists = contestRepository.existsById(contestId);
            boolean problemExists = problemRepository.existsById(updateProblemDto.getProblemId());
            boolean ioExists = ioRepository.existsById(updateProblemDto.getIoId());
            // 대회, 문제 여부 확인 후 문제 내용 저장
            if (contestExists && problemExists && ioExists) {
                updateProblemService.updateProblem(updateProblemDto);
                updateIOService.UpdateIO(updateProblemDto);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}