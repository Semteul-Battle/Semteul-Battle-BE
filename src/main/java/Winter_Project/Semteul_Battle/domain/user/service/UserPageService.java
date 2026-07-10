package Winter_Project.Semteul_Battle.domain.user.service;

import Winter_Project.Semteul_Battle.domain.user.dto.response.UserPageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserPageService {

    UserPageDto getUserInfoWithContests(String loginId);

    void setShowContestsVisibility(String loginId, boolean visible);

    String uploadUserProfilePic(MultipartFile file) throws IOException;

    void deleteUserProfilePic(String fileUrl);
}
