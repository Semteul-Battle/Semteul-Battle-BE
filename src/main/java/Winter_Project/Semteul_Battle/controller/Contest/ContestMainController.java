package Winter_Project.Semteul_Battle.controller.Contest;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.*;
import Winter_Project.Semteul_Battle.dto.Contest.*;
import Winter_Project.Semteul_Battle.repository.*;
import Winter_Project.Semteul_Battle.service.Contest.ContestLiveService;
import Winter_Project.Semteul_Battle.service.Contest.ContestService;
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
    private final ProblemRepository problemRepository;
    private final ContestNoticeRepository contestNoticeRepository;
    private final UserRepository userRepository;
    private final ContestQuestionRepository contestQuestionRepository;
    private final ContestRepository contestRepository;

    // 실시간 대회 - 참가자 권한
    @GetMapping("/contestMain")
    public String contestMain(@RequestParam("contestId") Long contestId,
                              @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출
        Long HeIs = contestLiveService.whoAreU(contestId, tokenFromId);

        if (HeIs == 0)
            return "examiner";
        else if (HeIs == 1)
            return "contestant";
        else
            return "idk";
    }

    // 실시간 대회 - 문제 리스트
    @GetMapping("/problemList")
    public List<ContestInfoDTO> getProblemsByContestId(@RequestParam Long contestId,
                                                       @RequestHeader("Authorization") String token) {
        return contestLiveService.getProblemsByContestId(contestId);
    }

    // 실시간 대회 - 문제 객체 넘기기
    @GetMapping("/problemInfo")
    public ResponseEntity<List<Problem>> getProblemsInfo(@RequestParam Long contestId,
                                                         @RequestHeader("Authorization") String token) {
        List<Problem> problems = contestLiveService.getProblemsInfo(contestId);
        return ResponseEntity.ok(problems);
    }

    // 실시간 대회 - 공지사항 불러오기
    @GetMapping("/contestNotice")
    public List<ContestNotice> getContestNoticeByContestId(@RequestParam Long contestId,
                                                           @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출

        List<ContestNotice> notices = contestLiveService.getContestNoticeByContestId(contestId, tokenFromId);

        return notices != null ? notices : Collections.emptyList();
    }

    // 실시간 대회 - 공지사항 글쓰기
    @PostMapping("/contestNoticeCreate")
    public ResponseEntity<ContestNotice> createContestNotice(@RequestBody ContestNoticeDTO contestNoticeDTO,
                                                             @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출
        Optional<Users> userOptional = userRepository.findByLoginId(tokenFromId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            Long userId = user.getId();

            contestNoticeDTO.setUserId(userId);
        }

        ContestNotice savedNotice = contestLiveService.saveContestNotice(contestNoticeDTO);
        return new ResponseEntity<>(savedNotice, HttpStatus.CREATED);
    }

    // 실시간 대회 - 공지사항 삭제
    @DeleteMapping("/contestNotices/{contestId}/{contestNoticeId}")
    public ResponseEntity<String> deleteContestNotice(@PathVariable("contestId") Long contestId,
                                                      @PathVariable("contestNoticeId") Long contestNoticeId,
                                                      @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출
        Long HeIs = contestLiveService.whoAreU(contestId, tokenFromId);

        if (HeIs == 0) {
            contestLiveService.deleteContestNotice(contestNoticeId);
            return ResponseEntity.ok("Contest Notice with ID: " + contestNoticeId + " has been deleted.");
        } else
            return ResponseEntity.ok("권한이 없습니다.");
    }

    // 대회 참가자의 isChecked 상태 확인
    @GetMapping("/isChecked")
    public ResponseEntity<Boolean> isChecked(@RequestParam("contestId") Long contestId,
                                             @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출
        Long userId = userRepository.findByLoginId(tokenFromId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + tokenFromId))
                .getId();

        boolean isContestantChecked = contestLiveService.isCheckedReturn(contestId, userId);
        return ResponseEntity.ok(isContestantChecked);
    }

    // 실시간 대회 - 제출 현황
    @GetMapping("/submitList")
    public ResponseEntity<SubmitPageDto<SubmitDTO>> getSubmitsList(@RequestParam Long contestId,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestHeader("Authorization") String token) {
        Pageable pageable = PageRequest.of(page, size);
        SubmitPageDto<SubmitDTO> submitPageDto = contestLiveService.getSubmitsWithProblems(contestId, pageable);
        return ResponseEntity.ok(submitPageDto);
    }


    // 질문 게시판 불러오기
    @GetMapping("/questionsList/{contestId}")
    public List<ContestQuestion> getQuestionsByContestId(@PathVariable Long contestId,
                                                          @RequestHeader("Authorization") String token) {

        Contest contest = contestService.getContestById(contestId);
        return contestLiveService.getQuestionsByContest(contest);
    }

    // 질문 게시판에 글쓰기
    @PostMapping("/createQuestions")
    public ResponseEntity<String> addQuestion(@RequestBody ContestQuestionDTO contestQuestionDTO,
                                              @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출
        Optional<Users> userOptional = userRepository.findByLoginId(tokenFromId);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            Long userId = user.getId();

            // 질문자 ID 설정
            contestQuestionDTO.setUserId(userId);

            contestLiveService.addQuestion(contestQuestionDTO);

            return new ResponseEntity<>("Question added successfully", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // 질문 게시판에서 글 삭제하기
    @DeleteMapping("/deleteQuestion/{questionId}")
    public ResponseEntity<String> deleteContestQuestion(@PathVariable Long questionId,
                                                        @RequestHeader("Authorization") String token) {

        contestLiveService.deleteContestQuestion(questionId);
        return new ResponseEntity<>("Question delete successfully", HttpStatus.CREATED);
    }

    // 질문 게시판 댓글 달기
    @PostMapping("/QuestionAnswer")
    public ResponseEntity<String> answerQuestion(@RequestBody AnswerDTO answerDTO,
                                                 @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출
        Optional<Users> userOptional = userRepository.findByLoginId(tokenFromId);

        if (userOptional.isPresent()) {
            Users answerer = userOptional.get();
            Long userId = answerer.getId();

            // 답변자의 ID 설정
            answerDTO.setAnswerer(userId);

            return contestLiveService.answerQuestion(answerDTO);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

}