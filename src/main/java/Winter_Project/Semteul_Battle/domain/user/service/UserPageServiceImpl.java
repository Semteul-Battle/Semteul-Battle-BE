package Winter_Project.Semteul_Battle.domain.user.service;



import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.domain.user.exception.UserException;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contestant;
import Winter_Project.Semteul_Battle.domain.contest.entity.ContestantContest;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.user.dto.response.ContestInfoDto;
import Winter_Project.Semteul_Battle.domain.user.dto.response.UserPageDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestantContestRepository;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestantRepository;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserPageServiceImpl implements UserPageService {
    private final UserRepository userRepository;
    private final ContestantRepository contestantRepository;
    private final ContestantContestRepository contestantContestRepository;
    private final AmazonS3 amazonS3Client;

    @Value("#{environment['cloud.aws.s3.bucketName']}")
    private String bucket;


    @Transactional(readOnly = true)
    public UserPageDto getUserInfoWithContests(String loginId) {
        Optional<Users> userOptional = userRepository.findByLoginId(loginId);

        if (!userOptional.isPresent()) {
            return null;
        }

        Users user = userOptional.get();
        List<ContestInfoDto> contestInfoList = new ArrayList<>();
        boolean showContestVisibility = user.getView() == 1;


if (showContestVisibility) {
List<Contestant> contestants = contestantRepository.findByUsers_Id(user.getId());

            for (Contestant contestant : contestants) {
                List<ContestantContest> contestantContests = contestantContestRepository.findByContestant_Id(contestant.getId());

                for (ContestantContest cc : contestantContests) {
                    Contest contest = cc.getContest();
                    contestInfoList.add(ContestInfoDto.of(contest.getContestName(), contest.getEnterAuthority()));
                }
            }
        }

        return UserPageDto.from(user, contestInfoList);
    }


@Transactional
    public void setShowContestsVisibility(String loginId, boolean visible) {
        Optional<Users> userOptional = userRepository.findByLoginId(loginId);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.changeContestVisibility(visible);
        } else {
            throw new UserException(ErrorStatus._NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
    }


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
