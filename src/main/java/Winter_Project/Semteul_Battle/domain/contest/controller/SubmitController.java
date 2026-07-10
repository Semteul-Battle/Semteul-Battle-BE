package Winter_Project.Semteul_Battle.domain.contest.controller;

import Winter_Project.Semteul_Battle.domain.contest.dto.request.SubmitDTO;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestLiveService;
import Winter_Project.Semteul_Battle.domain.contest.service.SubmitService;
import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/submit")
public class SubmitController {

    private final SubmitService submitService;
    private final ContestLiveService contestLiveService;

    @PostMapping("/saveSubmit")
    public BaseResponse<Void> submitCode(
            @RequestBody SubmitDTO submitDTO,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        contestLiveService.whoAreU(submitDTO.getContestId(), loginId);
        Long savedSubmitId = submitService.saveSubmit(submitDTO);
        submitService.saveCodeToFile(submitDTO, savedSubmitId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }
}
