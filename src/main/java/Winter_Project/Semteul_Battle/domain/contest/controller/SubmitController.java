package Winter_Project.Semteul_Battle.domain.contest.controller;

import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.contest.dto.SubmitDTO;
import Winter_Project.Semteul_Battle.domain.contest.service.ContestLiveService;
import Winter_Project.Semteul_Battle.domain.contest.service.SubmitService;
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

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // ????影?력??????loginId ?????댄뱼???
Long HeIs = contestLiveService.whoAreU(submitDTO.getContestId(), tokenFromId); // ??? ?????諛몃마??維◈??
        // DB??????
Long savedSubmitId = submitService.saveSubmit(submitDTO);

        // ?????얜궙??뺣뙀?洹?㎦????????????????ID????????꿔꺂?????????????띾쐭??????& ??????????諛몃마??λ??????얜?沅싷┼??뀕????????????? & ??棺堉?뤃???????곗뵯??? ????⑥ル럯???????⑥ロ꺘??        submitService.saveCodeToFile(submitDTO, savedSubmitId);

        return new ResponseEntity<>("Code submitted successfully!", HttpStatus.OK);
    }

}
