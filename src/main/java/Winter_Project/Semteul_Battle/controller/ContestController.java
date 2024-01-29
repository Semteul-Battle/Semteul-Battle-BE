package Winter_Project.Semteul_Battle.controller;

import Winter_Project.Semteul_Battle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contest")
public class ContestController {
    // 대회 생성은 ADMIN만 가능

    private final UserRepository userRepository;

    // 대회 생성 - 출제자 지정
    @GetMapping("/id-designate")
    public boolean idDesignate(@RequestParam("loginId") String loginId,
                               @RequestHeader("Authorization") String token) {

        return userRepository.existsByLoginId(loginId);
    }
}
