package Winter_Project.Semteul_Battle.domain.problem.controller;

import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestLiveService;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.AddIODto;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.AddIOFileDto;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.AddProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.DeleteProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.UpdateProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.exception.ProblemException;
import Winter_Project.Semteul_Battle.domain.problem.repository.IORepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;
import Winter_Project.Semteul_Battle.domain.problem.service.AddIOService;
import Winter_Project.Semteul_Battle.domain.problem.service.AddProblemService;
import Winter_Project.Semteul_Battle.domain.problem.service.DeleteIOService;
import Winter_Project.Semteul_Battle.domain.problem.service.DeleteProblemService;
import Winter_Project.Semteul_Battle.domain.problem.service.UpdateIOService;
import Winter_Project.Semteul_Battle.domain.problem.service.UpdateProblemService;
import Winter_Project.Semteul_Battle.domain.problem.service.UploadPictureService;
import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contests")
public class ProblemController {

    private final AddProblemService addProblemService;
    private final DeleteProblemService deleteProblemService;
    private final DeleteIOService deleteIOService;
    private final ProblemRepository problemRepository;
    private final ContestRepository contestRepository;
    private final ContestLiveService contestLiveService;
    private final UpdateProblemService updateProblemService;
    private final UpdateIOService updateIOService;
    private final IORepository ioRepository;
    private final AddIOService addIOService;
    private final UploadPictureService uploadPictureService;

    @PostMapping("/addProblem")
    public boolean addProblem(@RequestBody @Valid AddProblemDto addProblemDto,
                              @AuthenticationPrincipal(expression = "username") String loginId) {
        validateExaminer(addProblemDto.getContestId(), loginId);
        addProblemService.problemFrame(addProblemDto);
        return true;
    }

    @PostMapping("/addIO")
    public boolean addIO(@RequestBody @Valid AddIODto addIODto,
                         @AuthenticationPrincipal(expression = "username") String loginId) {
        validateExaminer(addIODto.getContestId(), loginId);
        addIOService.IOFrame(addIODto);
        return true;
    }

    @DeleteMapping("/deleteProblem")
    public boolean deleteProblem(@RequestBody @Valid DeleteProblemDto deleteProblemDto,
                                 @AuthenticationPrincipal(expression = "username") String loginId) {
        validateExaminer(deleteProblemDto.getContestId(), loginId);
        validateProblemTargets(deleteProblemDto.getContestId(), deleteProblemDto.getProblemId(), deleteProblemDto.getIoId());
        deleteIOService.deleteIO(deleteProblemDto);
        deleteProblemService.deleteProblem(deleteProblemDto);
        return true;
    }

    @PatchMapping("/updateProblem")
    public boolean updateProblem(@RequestBody @Valid UpdateProblemDto updateProblemDto,
                                 @AuthenticationPrincipal(expression = "username") String loginId) {
        validateExaminer(updateProblemDto.getContestId(), loginId);
        validateProblemTargets(updateProblemDto.getContestId(), updateProblemDto.getProblemId(), updateProblemDto.getIoId());
        updateProblemService.updateProblem(updateProblemDto);
        updateIOService.UpdateIO(updateProblemDto);
        return true;
    }

    @PostMapping("/addFile")
    public BaseResponse<Void> generateFiles(@RequestBody @Valid AddIOFileDto addIOFileDto,
                                            @AuthenticationPrincipal(expression = "username") String loginId) {
        validateExaminer(addIOFileDto.getContestId(), loginId);
        addProblemService.createInputOutputFiles(
                addIOFileDto.getInputFile(),
                addIOFileDto.getOutputFile(),
                addIOFileDto.getNumber()
        );
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @PostMapping("/uploadPictures")
    public List<String> uploadPictures(@AuthenticationPrincipal(expression = "username") String loginId,
                                       @RequestParam("contestId") Long contestId,
                                       @RequestParam("problemId") Long problemId,
                                       @RequestPart("files") List<MultipartFile> files) {
        validateExaminer(contestId, loginId);
        try {
            return uploadPictureService.uploadPictures(files, problemId);
        } catch (IOException e) {
            log.error("Failed to upload pictures: {}", e.getMessage());
            throw new ProblemException(ErrorStatus._INTERNAL_SERVER_ERROR, "문제 이미지 업로드에 실패했습니다.");
        }
    }

    @PatchMapping("/updatePictures")
    public List<String> updatePictures(@AuthenticationPrincipal(expression = "username") String loginId,
                                       @RequestParam("problemId") Long problemId,
                                       @RequestParam("contestId") Long contestId,
                                       @RequestPart("files") List<MultipartFile> files) {
        validateExaminer(contestId, loginId);
        try {
            List<String> updatedImageUrls = uploadPictureService.updatePictures(files, problemId);
            if (updatedImageUrls == null || updatedImageUrls.isEmpty()) {
                throw new ProblemException(ErrorStatus._BAD_REQUEST, "수정된 이미지가 없습니다.");
            }
            return updatedImageUrls;
        } catch (IOException e) {
            log.error("Failed to update pictures: {}", e.getMessage());
            throw new ProblemException(ErrorStatus._INTERNAL_SERVER_ERROR, "문제 이미지 수정에 실패했습니다.");
        }
    }

    private void validateExaminer(Long contestId, String loginId) {
        Long participantStatus = contestLiveService.whoAreU(contestId, loginId);
        if (participantStatus != 0) {
            throw new ProblemException(ErrorStatus._FORBIDDEN, "문제를 관리할 권한이 없습니다.");
        }
    }

    private void validateProblemTargets(Long contestId, Long problemId, Long ioId) {
        if (!contestRepository.existsById(contestId)) {
            throw new ProblemException(ErrorStatus._NOT_FOUND, "대회를 찾을 수 없습니다.");
        }
        if (!problemRepository.existsById(problemId)) {
            throw new ProblemException(ErrorStatus._NOT_FOUND, "문제를 찾을 수 없습니다.");
        }
        if (!ioRepository.existsById(ioId)) {
            throw new ProblemException(ErrorStatus._NOT_FOUND, "입출력 예제를 찾을 수 없습니다.");
        }
    }
}
