package Winter_Project.Semteul_Battle.domain.contest.controller;

import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestApplicationDto;
import Winter_Project.Semteul_Battle.domain.contest.exception.ContestException;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestApplicationService;
import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contests")
public class ContestApplicationController {

    private final ContestApplicationService contestApplicationService;

    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> applyContest(
            @RequestBody ContestApplicationDto contestApplicationDto,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        boolean result = contestApplicationService.applyContest(contestApplicationDto, loginId);
        if (!result) {
            throw new ContestException(ErrorStatus._BAD_REQUEST, "대회 신청에 실패했습니다.");
        }
        return BaseResponse.onSuccess(SuccessStatus.CREATED, null);
    }
}
