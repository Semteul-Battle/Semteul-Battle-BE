package Winter_Project.Semteul_Battle.domain.contest.controller;

import Winter_Project.Semteul_Battle.domain.contest.dto.request.CreateContestDto;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.Examiner;
import Winter_Project.Semteul_Battle.domain.contest.exception.ContestException;
import Winter_Project.Semteul_Battle.domain.contest.repository.ExaminerRepository;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestService;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.domain.user.service.UserService;
import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contest")
public class ContestController {

    private final UserRepository userRepository;
    private final ContestService contestService;
    private final UserService userService;
    private final ExaminerRepository examinerRepository;

    @GetMapping("/id-designate")
    public BaseResponse<String> idDesignate(@RequestParam("loginId") String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            return BaseResponse.onSuccess(SuccessStatus.OK, loginId);
        }
        throw new ContestException(ErrorStatus._NOT_FOUND, "사용자를 찾을 수 없습니다.");
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @PostMapping("/contestCreate")
    public BaseResponse<Void> contestCreate(@RequestBody @Valid CreateContestDto createContestDto) {
        try {
            Contest createdContest = contestService.createContest(createContestDto);
            Long contestId = createdContest.getId();

            for (String username : createContestDto.getExaminerUsernames()) {
                Users user = userService.getUserByUsername(username);
                Examiner examiner = Examiner.builder()
                        .users(user)
                        .contest(new Contest(contestId))
                        .build();
                examinerRepository.save(examiner);
            }
            return BaseResponse.onSuccess(SuccessStatus.CREATED, null);
        } catch (Exception e) {
            log.error("Failed to create contest", e);
            throw new ContestException(ErrorStatus._INTERNAL_SERVER_ERROR, "대회 생성에 실패했습니다.");
        }
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @DeleteMapping("/contestDelete/{contestId}")
    public BaseResponse<Void> contestDelete(@PathVariable("contestId") Long contestId) {
        try {
            contestService.deleteContest(contestId);
            return BaseResponse.onSuccess(SuccessStatus.OK, null);
        } catch (Exception e) {
            log.error("Failed to delete contest", e);
            throw new ContestException(ErrorStatus._INTERNAL_SERVER_ERROR, "대회 삭제에 실패했습니다.");
        }
    }
}
