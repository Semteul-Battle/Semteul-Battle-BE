package Winter_Project.Semteul_Battle.domain.contest.controller;

import Winter_Project.Semteul_Battle.domain.contest.dto.request.AnswerDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestNoticeDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestQuestionDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.SubmitDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestInfoDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestNoticeResponseDto;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestQuestionResponseDto;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.SubmitPageDto;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.exception.ContestException;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestLiveService;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestService;
import Winter_Project.Semteul_Battle.domain.problem.dto.response.ProblemDetailResponseDto;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contests")
public class ContestMainController {

    private final ContestService contestService;
    private final ContestLiveService contestLiveService;
    private final UserRepository userRepository;

    @GetMapping("/contestMain")
    public BaseResponse<String> contestMain(
            @RequestParam("contestId") Long contestId,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        Long role = contestLiveService.whoAreU(contestId, loginId);
        if (role == 0) {
            return BaseResponse.onSuccess(SuccessStatus.OK, "examiner");
        }
        if (role == 1) {
            return BaseResponse.onSuccess(SuccessStatus.OK, "contestant");
        }
        return BaseResponse.onSuccess(SuccessStatus.OK, "unknown");
    }

    @GetMapping("/problemList")
    public List<ContestInfoDTO> getProblemsByContestId(
            @RequestParam Long contestId
    ) {
        return contestLiveService.getProblemsByContestId(contestId);
    }

    @GetMapping("/problemInfo")
    public List<ProblemDetailResponseDto> getProblemsInfo(
            @RequestParam Long contestId
    ) {
        return contestLiveService.getProblemsInfo(contestId);
    }

    @GetMapping("/contestNotice")
    public List<ContestNoticeResponseDto> getContestNoticeByContestId(
            @RequestParam Long contestId,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        List<ContestNoticeResponseDto> notices = contestLiveService.getContestNoticeByContestId(contestId, loginId);
        return notices != null ? notices : Collections.emptyList();
    }

    @PostMapping("/contestNoticeCreate")
    @ResponseStatus(HttpStatus.CREATED)
    public ContestNoticeResponseDto createContestNotice(
            @RequestBody @Valid ContestNoticeDTO contestNoticeDTO,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        Users user = getLoginUser(loginId);
        contestNoticeDTO.assignUserId(user.getId());
        return contestLiveService.saveContestNotice(contestNoticeDTO);
    }

    @DeleteMapping("/contestNotices/{contestId}/{contestNoticeId}")
    public BaseResponse<Void> deleteContestNotice(
            @PathVariable("contestId") Long contestId,
            @PathVariable("contestNoticeId") Long contestNoticeId,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        Long role = contestLiveService.whoAreU(contestId, loginId);
        if (role != 0) {
            throw new ContestException(ErrorStatus._FORBIDDEN, "대회 공지를 삭제할 권한이 없습니다.");
        }

        contestLiveService.deleteContestNotice(contestNoticeId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @GetMapping("/isChecked")
    public Boolean isChecked(
            @RequestParam("contestId") Long contestId,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        Users user = getLoginUser(loginId);
        return contestLiveService.isCheckedReturn(contestId, user.getId());
    }

    @GetMapping("/submitList")
    public SubmitPageDto<SubmitDTO> getSubmitsList(
            @RequestParam Long contestId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return contestLiveService.getSubmitsWithProblems(contestId, pageable);
    }

    @GetMapping("/questionsList/{contestId}")
    public List<ContestQuestionResponseDto> getQuestionsByContestId(
            @PathVariable Long contestId
    ) {
        Contest contest = contestService.getContestById(contestId);
        return contestLiveService.getQuestionsByContest(contest);
    }

    @PostMapping("/createQuestions")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> addQuestion(
            @RequestBody @Valid ContestQuestionDTO contestQuestionDTO,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        Users user = getLoginUser(loginId);
        contestQuestionDTO.assignUserId(user.getId());
        contestLiveService.addQuestion(contestQuestionDTO);
        return BaseResponse.onSuccess(SuccessStatus.CREATED, null);
    }

    @DeleteMapping("/deleteQuestion/{questionId}")
    public BaseResponse<Void> deleteContestQuestion(
            @PathVariable Long questionId
    ) {
        contestLiveService.deleteContestQuestion(questionId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @PostMapping("/QuestionAnswer")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> answerQuestion(
            @RequestBody @Valid AnswerDTO answerDTO,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        Users answerer = getLoginUser(loginId);
        answerDTO.assignAnswerer(answerer.getId());
        contestLiveService.answerQuestion(answerDTO);
        return BaseResponse.onSuccess(SuccessStatus.CREATED, null);
    }

    private Users getLoginUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ContestException(ErrorStatus._NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
