package Winter_Project.Semteul_Battle.controller.UserPage;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.UserPage.UserPageDto;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import Winter_Project.Semteul_Battle.service.UserPage.UserPageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/contests")
public class UsersPageController {

    private final UserPageService userPageService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("#{environment['cloud.aws.s3.bucketName']}")
    private String bucket;

    // 마이페이지 조회
    @GetMapping("/userPage")
    public ResponseEntity<UserPageDto> getUserPageInfo(@RequestHeader("Authorization") String token) {

        UserPageDto userPageDto = userPageService.getUserInfoWithContests(token);

        if (userPageDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userPageDto);
    }

    // 대회전적 숨김 기능
    @PutMapping("/showContests")
    public ResponseEntity<String> setShowContestsVisibility(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "visible", defaultValue = "true") boolean visible) {
        userPageService.setShowContestsVisibility(token, visible);
        return ResponseEntity.ok("대회 기록 표시 설정이 변경되었습니다.");
    }

    // 프로필 이미지 넣기
    @PostMapping("/userPic")
    public ResponseEntity<String> setShowContestsVisibility(@RequestHeader("Authorization") String token,
                                                            @RequestParam("file") MultipartFile file) {
        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출
        Optional<Users> userOptional = userRepository.findByLoginId(tokenFromId);

        if (userOptional.isPresent()) {
            try {
                String fileUrl = userPageService.uploadUserProfilePic(file);
                Users user = userOptional.get();
                user.saveProfileUrl(fileUrl); // Users 엔티티에 정의된 메소드를 호출하여 프로필 URL 저장
                userRepository.save(user); // 변경된 유저 엔티티를 저장합니다.

                return ResponseEntity.ok(fileUrl + " : 프로필 이미지 설정이 완료되었습니다.");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    // 프로필 이미지 수정
    @PutMapping("/userPicEdit")
    public ResponseEntity<String> updateUserProfilePic(@RequestHeader("Authorization") String token,
                                                       @RequestParam("file") MultipartFile file) {

        String tokenFromId = jwtTokenProvider.extractLoginIdFromToken(token); // 토큰에서 loginId 추출
        Optional<Users> userOptional = userRepository.findByLoginId(tokenFromId);

        if (userOptional.isPresent()) {
            try {
                Users user = userOptional.get();
                // 기존 프로필 사진 삭제
                if (user.getProfile() != null && !user.getProfile().isEmpty()) {
                    userPageService.deleteUserProfilePic(user.getProfile());
                }

                String fileUrl = userPageService.uploadUserProfilePic(file);
                user.setProfile(fileUrl);
                userRepository.save(user);

                return ResponseEntity.ok(fileUrl + " : 프로필 이미지가 업데이트되었습니다.");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

}