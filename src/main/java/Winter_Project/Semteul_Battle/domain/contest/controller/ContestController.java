package Winter_Project.Semteul_Battle.domain.contest.controller;

import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.Examiner;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.contest.dto.CreateContestDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.ExaminerRepository;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestService;
import Winter_Project.Semteul_Battle.domain.user.service.UserService;
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
    // ????????ш끽維뽳쭩???? ADMIN???????ル뒌????
private final UserRepository userRepository;
    private final ContestService contestService;
    private final UserService userService;
    private final ExaminerRepository examinerRepository;
    private final JwtTokenProvider jwtTokenProvider;


    // ????????ш끽維뽳쭩???- ????????????耀붾굝???????
@GetMapping("/id-designate")
    public String idDesignate(@RequestParam("loginId") String loginId,
                              @RequestHeader("Authorization") String token) {

        if (userRepository.existsByLoginId(loginId)) {
            return loginId;
        }
        return "false";

    }

    // ????????ш끽維뽳쭩???? - ADMIN
@Secured("ROLE_ADMIN")
    @Transactional
    @PostMapping("/contestCreate")
    public String contestCreate(@RequestBody CreateContestDto createContestDto,
                                @RequestHeader("Authorization") String token) {
        try {
            // ????????ш끽維뽳쭩???& id ????ш끽維뽳쭩???
Contest createContest = contestService.createContest(createContestDto);
            Long contestId = createContest.getId();

            for (String username : createContestDto.getExaminerUsernames()) {
                Users user = userService.getUserByUsername(username);
                Examiner examiner = Examiner.builder()
                        .users(user)
                        .contest(new Contest(contestId)) // ?????????????????????????댄뱼?????????꾨굴??
.build();

                examinerRepository.save(examiner);
            }
            return "contest create success";
        } catch (Exception e) {
            log.error("Failed to create contest", e);
            return "contest create failed";
        }
    }

    // ????????- ADMIN
@Secured("ROLE_ADMIN")
    @Transactional
    @DeleteMapping("/contestDelete/{contestId}")
    public String contestDelete(@PathVariable("contestId") Long contestId,
                                @RequestHeader("Authorization") String token) {

        System.out.println("contestId = " + contestId);
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token);
        System.out.println(tokenFromId);

        try {
            // ?????????????饔낅떽????????????
contestService.deleteContest(contestId);
return "contest delete success";
        } catch (Exception e) {
            log.error("Failed to delete contest", e);
            return "contest delete failed";
        }
    }
}