package Winter_Project.Semteul_Battle.service.UserPage;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Contestant;
import Winter_Project.Semteul_Battle.domain.ContestantContest;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.UserPage.ContestInfoDto;
import Winter_Project.Semteul_Battle.dto.UserPage.UserPageDto;
import Winter_Project.Semteul_Battle.repository.ContestantContestRepository;
import Winter_Project.Semteul_Battle.repository.ContestantRepository;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserPageService {
    private final UserRepository userRepository;
    private final ContestantRepository contestantRepository;
    private final ContestantContestRepository contestantContestRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AmazonS3 amazonS3Client;

    @Value("#{environment['cloud.aws.s3.bucketName']}")
    private String bucket;


    @Transactional(readOnly = true)
    public UserPageDto getUserInfoWithContests(String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> userOptional = userRepository.findByLoginId(loginId);

        if (!userOptional.isPresent()) {
            return null;
        }

        Users user = userOptional.get();
        UserPageDto userPageDto = new UserPageDto();
        userPageDto.setUserName(user.getName());
        userPageDto.setLoginId(user.getLoginId());
        userPageDto.setUniversity(user.getUniversity());
        userPageDto.setMajor(user.getMajor());
        userPageDto.setProfile(user.getProfile());

        // 대회 기록 숨김 설정 가져오기
        boolean showContestVisibility = user.getView() == 1;

        // 대회 기록 숨김 설정이 true일 때만 대회 정보를 가져오도록 처리
        if (showContestVisibility) {
            List<Contestant> contestants = contestantRepository.findByUsers_Id(user.getId());
            List<ContestInfoDto> contestInfoList = new ArrayList<>();

            for (Contestant contestant : contestants) {
                List<ContestantContest> contestantContests = contestantContestRepository.findByContestant_Id(contestant.getId());

                for (ContestantContest cc : contestantContests) {
                    Contest contest = cc.getContest();
                    ContestInfoDto contestInfoDto = new ContestInfoDto();
                    contestInfoDto.setContestName(contest.getContestName());
                    contestInfoDto.setEnterAuthority(contest.getEnterAuthority());
                    contestInfoList.add(contestInfoDto);
                }
            }

            userPageDto.setContestList(contestInfoList);
        }

        return userPageDto;
    }

    // 대회전적 숨김 기능
    @Transactional
    public void setShowContestsVisibility(String token, boolean visible) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> userOptional = userRepository.findByLoginId(loginId);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setView(visible ? 1 : 0); // visible 값에 따라 view 필드를 설정
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
    }

    // 프로필 설정
    @Transactional
    public String uploadUserProfilePic(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileUrl = "https://" + bucket + ".s3.amazonaws.com/userProfile/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, "userProfile/" + fileName, file.getInputStream(), metadata);

        return fileUrl;
    }

    public void deleteUserProfilePic(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        amazonS3Client.deleteObject(bucket, "userProfile/" + fileName);
    }

}