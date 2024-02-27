package Winter_Project.Semteul_Battle.controller.Problem;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.dto.Problem.*;
import Winter_Project.Semteul_Battle.repository.ContestRepository;
import Winter_Project.Semteul_Battle.repository.IORepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import Winter_Project.Semteul_Battle.service.Contest.ContestLiveService;
import Winter_Project.Semteul_Battle.service.Problem.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contests")
public class ProblemController {
    private final AddProblemService addProblemService;
    private final JwtTokenProvider  jwtTokenProvider;
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

    // 공백 문제 추가
    @PostMapping("/addProblem")
    public boolean addProblem(@RequestBody AddProblemDto addProblemDto,
                              @RequestHeader("Authorization") String token) {
        Long contestId = addProblemDto.getContestId();
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestLiveService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        if (participantStatus == 0) { // 출제자가 맞는 경우
            addProblemService.problemFrame(addProblemDto);
            return true;
        } else { // 출제자가 아닌 경우
            return false;
        }
    }
    //공백 예시 추가
    @PostMapping("/addIO")
    public boolean addIO(@RequestBody AddIODto addIODto,
                         @RequestHeader("Authorization") String token) {
        Long contestId = addIODto.getContestId();
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestLiveService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        if (participantStatus == 0) { // 출제자가 맞는 경우
            addIOService.IOFrame(addIODto);
            return true;
        } else { // 출제자가 아닌 경우
            return false;
        }
    }

    // 문제 삭제
    @DeleteMapping("/deleteProblem")
    public boolean deleteProblem(@RequestBody DeleteProblemDto deleteProblemDto,
                                 @RequestHeader("Authorization") String token) {
        Long contestId = deleteProblemDto.getContestId();
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestLiveService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        // 출제자 여부 확인
        if (participantStatus == 0) {
            // 해당 대회와 문제가 존재하는지 확인
            boolean contestExists = contestRepository.existsById(contestId);
            boolean problemExists = problemRepository.existsById(deleteProblemDto.getProblemId());
            boolean ioExists = ioRepository.existsById(deleteProblemDto.getIoId());
            // 대회와 문제가 모두 존재하는 경우에만 문제 삭제 서비스 호출
            if (contestExists && problemExists && ioExists) {
                deleteIOService.deleteIO(deleteProblemDto);
                deleteProblemService.deleteProblem(deleteProblemDto);
                return true;
            } else {
                // 대회 또는 문제가 존재하지 않는 경우
                return false;
            }
        } else { // 출제자가 아닌 경우
            return false;
        }
    }

    // 문제 수정
    @PatchMapping("/updateProblem")
    public boolean updateProblem(@RequestBody UpdateProblemDto updateProblemDto,
                                 @RequestHeader("Authorization") String token) {
        Long contestId = updateProblemDto.getContestId();
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestLiveService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        // 출제자 여부 확인
        if (participantStatus == 0) {
            // 해당 대회와 문제가 존재하는지 확인
            boolean contestExists = contestRepository.existsById(contestId);
            boolean problemExists = problemRepository.existsById(updateProblemDto.getProblemId());
            boolean ioExists = ioRepository.existsById(updateProblemDto.getIoId());
            // 대회, 문제 여부 확인 후 문제 내용 저장
            if (contestExists && problemExists && ioExists) {
                updateProblemService.updateProblem(updateProblemDto);
                updateIOService.UpdateIO(updateProblemDto);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // 파일 생성
    @PostMapping("/addFile")
    public ResponseEntity<String> generateFiles(@RequestBody AddIOFileDto addIOFileDto,
                                                @RequestHeader("Authorization") String token) {
        Long contestId = addIOFileDto.getContestId();
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestLiveService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        if (participantStatus == 0) { // 출제자가 맞는 경우
            // 입력 및 출력 파일 생성
            addProblemService.createInputOutputFiles(addIOFileDto.getInputFile(), addIOFileDto.getOutputFile());
            return ResponseEntity.ok("Files generated successfully."); // 파일 생성 완료 메시지 반환
        } else { // 출제자가 아닌 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to perform this action."); // 권한 없음 에러 반환
        }
    }

    @PostMapping("/uploadPictures")
    public ResponseEntity<List<String>> uploadPictures(@RequestHeader("Authorization") String token,
                                                       @RequestParam("contestId") Long contestId,
                                                       @RequestParam("problemId") Long problemId,
                                                       @RequestPart("files") List<MultipartFile> files) {
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestLiveService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        if (participantStatus == 0) { // 출제자인 경우
            try {
                List<String> imageUrls = uploadPictureService.uploadPictures(files, problemId);
                return ResponseEntity.ok(imageUrls);
            } catch (IOException e) {
                log.error("Failed to upload pictures: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else { // 출제자가 아닌 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}