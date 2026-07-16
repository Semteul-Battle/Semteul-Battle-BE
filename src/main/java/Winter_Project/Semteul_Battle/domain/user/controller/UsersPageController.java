package Winter_Project.Semteul_Battle.domain.user.controller;

import Winter_Project.Semteul_Battle.domain.user.dto.response.UserPageDto;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.user.exception.UserException;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.domain.user.service.UserPageService;
import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contests")
public class UsersPageController {

    private final UserPageService userPageService;
    private final UserRepository userRepository;

    @GetMapping("/userPage")
    public UserPageDto getUserPageInfo(@AuthenticationPrincipal(expression = "username") String loginId) {
        UserPageDto userPageDto = userPageService.getUserInfoWithContests(loginId);
        if (userPageDto == null) {
            throw new UserException(ErrorStatus._NOT_FOUND, "사용자 페이지 정보를 찾을 수 없습니다.");
        }
        return userPageDto;
    }

    @PutMapping("/showContests")
    public BaseResponse<Void> setShowContestsVisibility(
            @AuthenticationPrincipal(expression = "username") String loginId,
            @RequestParam(value = "visible", defaultValue = "true") boolean visible
    ) {
        userPageService.setShowContestsVisibility(loginId, visible);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @PostMapping("/userPic")
    public BaseResponse<String> uploadUserProfilePic(
            @AuthenticationPrincipal(expression = "username") String loginId,
            @RequestParam("file") MultipartFile file
    ) {
        Users user = getLoginUser(loginId);
        try {
            String fileUrl = userPageService.uploadUserProfilePic(file);
            user.saveProfileUrl(fileUrl);
            userRepository.save(user);
            return BaseResponse.onSuccess(SuccessStatus.OK, fileUrl);
        } catch (IOException e) {
            throw new UserException(ErrorStatus._INTERNAL_SERVER_ERROR, "프로필 이미지 업로드에 실패했습니다.");
        }
    }

    @PatchMapping("/userPicEdit")
    public BaseResponse<String> updateUserProfilePic(
            @AuthenticationPrincipal(expression = "username") String loginId,
            @RequestParam("file") MultipartFile file
    ) {
        Users user = getLoginUser(loginId);
        try {
            if (user.getProfile() != null && !user.getProfile().isEmpty()) {
                userPageService.deleteUserProfilePic(user.getProfile());
            }

            String fileUrl = userPageService.uploadUserProfilePic(file);
            user.saveProfileUrl(fileUrl);
            userRepository.save(user);
            return BaseResponse.onSuccess(SuccessStatus.OK, fileUrl);
        } catch (IOException e) {
            throw new UserException(ErrorStatus._INTERNAL_SERVER_ERROR, "프로필 이미지 수정에 실패했습니다.");
        }
    }

    private Users getLoginUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UserException(ErrorStatus._NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
