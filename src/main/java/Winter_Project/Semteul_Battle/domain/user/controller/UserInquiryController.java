package Winter_Project.Semteul_Battle.domain.user.controller;

import Winter_Project.Semteul_Battle.domain.user.dto.response.UserInquiryDto;
import Winter_Project.Semteul_Battle.domain.user.exception.UserException;
import Winter_Project.Semteul_Battle.domain.user.service.UserService;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserInquiryController {

    private final UserService userService;

    @GetMapping("/inquiry")
    public UserInquiryDto getUserInformation(@AuthenticationPrincipal(expression = "username") String loginId) {
        UserInquiryDto userInquiryDto = userService.getUserInformation(loginId);
        if (userInquiryDto == null) {
            throw new UserException(ErrorStatus._NOT_FOUND, "사용자 정보를 찾을 수 없습니다.");
        }
        return userInquiryDto;
    }
}
