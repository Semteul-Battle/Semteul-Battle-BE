package Winter_Project.Semteul_Battle.service.UserPage;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Contestant;
import Winter_Project.Semteul_Battle.domain.ContestantContest;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.UserPage.UserPageDto;
import Winter_Project.Semteul_Battle.repository.ContestantContestRepository;
import Winter_Project.Semteul_Battle.repository.ContestantRepository;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = false)
    public UserPageDto getUserInfoWithContests(String token) {

        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        // 사용자 정보 가져오기
        Optional<Users> userOptional = userRepository.findByLoginId(loginId);
        if (!userOptional.isPresent()) {
            return null; // 사용자가 존재하지 않을 경우 처리
        }
        Users user = userOptional.get();

        // 사용자가 참가한 대회 정보 가져오기
        List<Contestant> contestants = contestantRepository.findByUsers_Id(user.getId());

        List<Contest> contests = new ArrayList<>();

        for (Contestant contestant : contestants) {

            List<ContestantContest> contestantContests = contestantContestRepository.findByContestant_Id(contestant.getId());

            for (ContestantContest cc : contestantContests) {

                Contest contest = cc.getContest();
                contests.add(contest);
            }

        }
        // UserPageDto 객체에 사용자 정보와 대회 정보 설정하여 반환
        return new UserPageDto(user, contests);
    }
}


