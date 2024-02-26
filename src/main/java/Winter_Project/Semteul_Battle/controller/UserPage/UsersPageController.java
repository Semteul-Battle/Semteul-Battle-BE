package Winter_Project.Semteul_Battle.controller.UserPage;

import Winter_Project.Semteul_Battle.dto.UserPage.UserPageDto;
import Winter_Project.Semteul_Battle.service.UserPage.UserPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contests")
public class UsersPageController {

    private final UserPageService userPageService;

    @GetMapping("/userPage")
    public ResponseEntity<UserPageDto> getUserPageInfo(@RequestHeader("Authorization") String token) {
        UserPageDto userPageDto = userPageService.getUserInfoWithContests(token);

        if (userPageDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userPageDto);
    }




}