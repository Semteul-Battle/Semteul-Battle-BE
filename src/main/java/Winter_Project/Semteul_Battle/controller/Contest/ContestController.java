package Winter_Project.Semteul_Battle.controller.Contest;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Examiner;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.Contest.CreateContestDto;
import Winter_Project.Semteul_Battle.repository.ExaminerRepository;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import Winter_Project.Semteul_Battle.service.Contest.ContestService;
import Winter_Project.Semteul_Battle.service.Users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contest")
public class ContestController {
    // 대회 생성은 ADMIN만 가능

    private final UserRepository userRepository;
    private final ContestService contestService;
    private final UserService userService;
    private final ExaminerRepository examinerRepository;
    private final JwtTokenProvider jwtTokenProvider;


    // 대회 생성 - 출제자 지정
    @GetMapping("/id-designate")
    public String idDesignate(@RequestParam("loginId") String loginId,
                              @RequestHeader("Authorization") String token) {

        if (userRepository.existsByLoginId(loginId)) {
            return loginId;
        }
        return "false";

    }

    // 대회 생성은 - ADMIN
    @Secured("ROLE_ADMIN")
    @Transactional
    @PostMapping("/contestCreate")
    public String contestCreate(@RequestBody CreateContestDto createContestDto,
                                @RequestHeader("Authorization") String token) {
        try {
            // 대회 생성 & id 생성
            Contest createContest = contestService.createContest(createContestDto);
            Long contestId = createContest.getId();

            for (String username : createContestDto.getExaminerUsernames()) {
                Users user = userService.getUserByUsername(username);
                Examiner examiner = Examiner.builder()
                        .users(user)
                        .contest(new Contest(contestId)) // 대회 출제자 테이블에 추가
                        .build();

                examinerRepository.save(examiner);
            }
            return "contest create success";
        } catch (Exception e) {
            log.error("Failed to create contest", e);
            return "contest create failed";
        }
    }

    // 대회 삭제 - ADMIN
    @Secured("ROLE_ADMIN")
    @Transactional
    @DeleteMapping("/contestDelete/{contestId}")
    public String contestDelete(@PathVariable("contestId") Long contestId,
                                @RequestHeader("Authorization") String token) {

        System.out.println("하이요" + contestId);
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token);
        System.out.println(tokenFromId);

        try {
            // 대회 및 연관된 정보 삭제
            contestService.deleteContest(contestId);
            return "contest delete success";
        } catch (Exception e) {
            log.error("Failed to delete contest", e);
            return "contest delete failed";
        }
    }
}