package Winter_Project.Semteul_Battle.controller.Users;

import Winter_Project.Semteul_Battle.dto.Users.UserInquiryDto;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import Winter_Project.Semteul_Battle.service.Users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserInquiryController {
    private final UserService userService;

    @GetMapping("/inquiryUser")
    public ResponseEntity<UserInquiryDto> getUserInformation(@RequestHeader("Authorization") String token) {

        UserInquiryDto userInquiryDto = userService.getUserInformation(token);

        if (userInquiryDto != null) {
            return new ResponseEntity<>(userInquiryDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}