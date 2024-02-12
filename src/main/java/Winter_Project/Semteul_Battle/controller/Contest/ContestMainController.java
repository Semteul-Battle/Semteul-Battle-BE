package Winter_Project.Semteul_Battle.controller.Contest;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.ContestNotice;
import Winter_Project.Semteul_Battle.domain.Problem;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.Contest.ContestInfoDTO;
import Winter_Project.Semteul_Battle.dto.Contest.ContestNoticeDTO;
import Winter_Project.Semteul_Battle.repository.ContestNoticeRepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import Winter_Project.Semteul_Battle.service.Contest.ContestLiveService;
import Winter_Project.Semteul_Battle.service.Contest.ContestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // 실시간 대회 - 공지사항 불러오기
    @GetMapping("/contestNotice")
    public List<ContestNotice> getContestNoticeByContestId(@RequestParam Long contestId,
                                                           @RequestHeader("Authorization") String token) {

        List<ContestNotice> notices = contestLiveService.getContestNoticeByContestId(contestId);

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
        System.out.println("들어옴요");

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출

        Long userId = userRepository.findByLoginId(tokenFromId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + tokenFromId))
                .getId();

        boolean isContestantChecked = contestLiveService.isCheckedReturn(contestId, userId);

        System.out.println("출력 값" + isContestantChecked);

        return ResponseEntity.ok(isContestantChecked);
    }


}