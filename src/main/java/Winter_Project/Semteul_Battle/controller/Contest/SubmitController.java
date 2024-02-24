package Winter_Project.Semteul_Battle.controller.Contest;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.dto.Contest.SubmitDTO;
import Winter_Project.Semteul_Battle.service.Contest.ContestLiveService;
import Winter_Project.Semteul_Battle.service.Contest.SubmitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/submit")
public class SubmitController {

    private final SubmitService submitService;
    private final ContestLiveService contestLiveService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/saveSubmit")
    public ResponseEntity<String> submitCode(@RequestBody SubmitDTO submitDTO,
                                             @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출
        Long HeIs = contestLiveService.whoAreU(submitDTO.getContestId(), tokenFromId); // 유저 아이디

        // DB에 저장
        Long savedSubmitId = submitService.saveSubmit(submitDTO);

        // 저장된 데이터의 ID를 사용하여 파일로 저장 & 채점 프로그램을 실행 & 결과까지 불러오기
        submitService.saveCodeToFile(submitDTO, savedSubmitId);

        return new ResponseEntity<>("Code submitted successfully!", HttpStatus.OK);
    }

}
