package Winter_Project.Semteul_Battle.controller.Contest;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.service.Contest.ContestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contests")
public class ContestMainController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ContestService contestService;

    // 실시간 대회 메인페이지 요청
    @GetMapping("/contestMain")
    public String contestMain(@RequestParam("contestId") Long contestId,
                              @RequestHeader("Authorization") String token) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 id 추출
        Long participantStatus = contestService.whoAreU(contestId, tokenFromId); // 참가자 / 출제자 구분

        if (participantStatus == 0) {
            return "대회 출제자 입니다.";
        } else if(participantStatus == 1) {
            return "대회 참가자가 입니다.";
        } else {
            return "대회 참가자가 아닙니다.";
        }

        // 문제 정보 띄우기

        // 해당 대회 공지 띄우기

    }

}
