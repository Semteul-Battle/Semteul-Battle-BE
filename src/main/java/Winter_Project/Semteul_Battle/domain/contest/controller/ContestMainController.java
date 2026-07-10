package Winter_Project.Semteul_Battle.domain.contest.controller;

import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import Winter_Project.Semteul_Battle.domain.contest.dto.*;
import Winter_Project.Semteul_Battle.domain.contest.repository.*;
import Winter_Project.Semteul_Battle.domain.problem.repository.*;
import Winter_Project.Semteul_Battle.domain.user.repository.*;
import Winter_Project.Semteul_Battle.domain.menu.repository.*;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestLiveService;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contests")
public class ContestMainController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ContestService contestService;
    private final ContestLiveService contestLiveService;
    private final UserRepository userRepository;

    // ??????살퓢???????- ?饔낅떽????怨뚮옩????????????
@GetMapping("/contestMain")
    public String contestMain(@RequestParam("contestId") Long contestId,
                              @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // ????影?력??????loginId ?????댄뱼???
Long HeIs = contestLiveService.whoAreU(contestId, tokenFromId);

        if (HeIs == 0)
            return "examiner";
        else if (HeIs == 1)
            return "contestant";
        else
            return "idk";
    }

    // ??????살퓢???????- ???嶺????????녾컯????욱렱嶺??
@GetMapping("/problemList")
    public List<ContestInfoDTO> getProblemsByContestId(@RequestParam Long contestId,
                                                       @RequestHeader("Authorization") String token) {
        return contestLiveService.getProblemsByContestId(contestId);
    }

    // ??????살퓢???????- ???嶺????????ル늉???????????ш끽維귞댆??
@GetMapping("/problemInfo")
    public ResponseEntity<List<Problem>> getProblemsInfo(@RequestParam Long contestId,
                                                         @RequestHeader("Authorization") String token) {
        List<Problem> problems = contestLiveService.getProblemsInfo(contestId);
        return ResponseEntity.ok(problems);
    }

    // ??????살퓢???????- ?????怨룹??????????⑥ル럯???????⑥ロ꺘??
@GetMapping("/contestNotice")
    public List<ContestNotice> getContestNoticeByContestId(@RequestParam Long contestId,
                                                           @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // ????影?력??????loginId ?????댄뱼???
List<ContestNotice> notices = contestLiveService.getContestNoticeByContestId(contestId, tokenFromId);

        return notices != null ? notices : Collections.emptyList();
    }

    // ??????살퓢???????- ?????怨룹???????????留??????怨뺤른??
@PostMapping("/contestNoticeCreate")
    public ResponseEntity<ContestNotice> createContestNotice(@RequestBody ContestNoticeDTO contestNoticeDTO,
                                                             @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // ????影?력??????loginId ?????댄뱼???
Optional<Users> userOptional = userRepository.findByLoginId(tokenFromId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            Long userId = user.getId();

            contestNoticeDTO.setUserId(userId);
        }

        ContestNotice savedNotice = contestLiveService.saveContestNotice(contestNoticeDTO);
        return new ResponseEntity<>(savedNotice, HttpStatus.CREATED);
    }

    // ??????살퓢???????- ?????怨룹??????????
@DeleteMapping("/contestNotices/{contestId}/{contestNoticeId}")
    public ResponseEntity<String> deleteContestNotice(@PathVariable("contestId") Long contestId,
                                                      @PathVariable("contestNoticeId") Long contestNoticeId,
                                                      @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // ????影?력??????loginId ?????댄뱼???
Long HeIs = contestLiveService.whoAreU(contestId, tokenFromId);

        if (HeIs == 0) {
            contestLiveService.deleteContestNotice(contestNoticeId);
            return ResponseEntity.ok("Contest Notice with ID: " + contestNoticeId + " has been deleted.");
        } else
            return ResponseEntity.ok("??????????????깅즽????????놁졄.");
    }

    // ?????饔낅떽????怨뚮옩????????isChecked ?????됰Ŧ鍮????轅붽틓????????
@GetMapping("/isChecked")
    public ResponseEntity<Boolean> isChecked(@RequestParam("contestId") Long contestId,
                                             @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // ????影?력??????loginId ?????댄뱼???
Long userId = userRepository.findByLoginId(tokenFromId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + tokenFromId))
                .getId();

        boolean isContestantChecked = contestLiveService.isCheckedReturn(contestId, userId);
        return ResponseEntity.ok(isContestantChecked);
    }

    // ??????살퓢???????- ???꿔꺂?????????熬곻퐢夷??
@GetMapping("/submitList")
    public ResponseEntity<SubmitPageDto<SubmitDTO>> getSubmitsList(@RequestParam Long contestId,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestHeader("Authorization") String token) {
        Pageable pageable = PageRequest.of(page, size);
        SubmitPageDto<SubmitDTO> submitPageDto = contestLiveService.getSubmitsWithProblems(contestId, pageable);
        return ResponseEntity.ok(submitPageDto);
    }


    // ?饔낅떽?????????彛???棺堉?뤃?????????⑥ル럯???????⑥ロ꺘??
@GetMapping("/questionsList/{contestId}")
    public List<ContestQuestion> getQuestionsByContestId(@PathVariable Long contestId,
                                                          @RequestHeader("Authorization") String token) {

        Contest contest = contestService.getContestById(contestId);
        return contestLiveService.getQuestionsByContest(contest);
    }

    // ?饔낅떽?????????彛???棺堉?뤃?????????????留??????怨뺤른??
@PostMapping("/createQuestions")
    public ResponseEntity<String> addQuestion(@RequestBody ContestQuestionDTO contestQuestionDTO,
                                              @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // ????影?력??????loginId ?????댄뱼???
Optional<Users> userOptional = userRepository.findByLoginId(tokenFromId);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            Long userId = user.getId();

            // ?饔낅떽?????????彛??ID ???嚥싲갭큔???            contestQuestionDTO.setUserId(userId);

            contestLiveService.addQuestion(contestQuestionDTO);

            return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // ?饔낅떽?????????彛???棺堉?뤃??????????????留? ????????ш끽維귞댆?
@DeleteMapping("/deleteQuestion/{questionId}")
    public ResponseEntity<String> deleteContestQuestion(@PathVariable Long questionId,
                                                        @RequestHeader("Authorization") String token) {

        contestLiveService.deleteContestQuestion(questionId);
        return new ResponseEntity<>("Question delete successfully", HttpStatus.CREATED);
    }

    // ?饔낅떽?????????彛???棺堉?뤃???????? ????
@PostMapping("/QuestionAnswer")
    public ResponseEntity<String> answerQuestion(@RequestBody AnswerDTO answerDTO,
                                                 @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // ????影?력??????loginId ?????댄뱼???
Optional<Users> userOptional = userRepository.findByLoginId(tokenFromId);

        if (userOptional.isPresent()) {
            Users answerer = userOptional.get();
            Long userId = answerer.getId();

            // ????????ID ???嚥싲갭큔???            answerDTO.setAnswerer(userId);

            return contestLiveService.answerQuestion(answerDTO);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

}