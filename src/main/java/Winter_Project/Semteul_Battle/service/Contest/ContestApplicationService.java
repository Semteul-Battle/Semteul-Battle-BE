package Winter_Project.Semteul_Battle.service.Contest;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Contestant;
import Winter_Project.Semteul_Battle.domain.ContestantContest;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.Contest.ContestApplicationDto;
import Winter_Project.Semteul_Battle.repository.ContestRepository;
import Winter_Project.Semteul_Battle.repository.ContestantContestRepository;
import Winter_Project.Semteul_Battle.repository.ContestantRepository;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContestApplicationService {
    private final ContestRepository contestRepository;
    private final ContestantRepository contestantRepository;
    private final ContestantContestRepository contestantContestRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public boolean applyContest(ContestApplicationDto contestApplicationDto, String token) {

        // Contestant 객체 생성하여 사용자 정보 설정
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUser = userRepository.findByLoginId(loginId);
        // Contestant 엔티티 생성
        Contestant contestant = new Contestant();
        contestant.setUsers(optionalUser.get());

        // 대회 이름 가져오기
        Optional<Contest> ContestName = contestRepository.findByContestName(contestApplicationDto.getContestName());
        if (ContestName.isEmpty()) {
            log.error("대회 정보를 찾을 수 없습니다.");
            return false;
        }
        Contest contest = ContestName.get();
        Contestant savedContestant = contestantRepository.save(contestant);

//         대회에 참가한 사용자와 대회 정보를 ContestantContest 엔티티에 저장
        ContestantContest contestantContest = new ContestantContest();
        contestantContest.setContest(contest);
        contestantContest.setContestant(savedContestant);

        // ContestantContest 엔티티 저장
        contestantContestRepository.save(contestantContest);

        return true; // 대회 신청이 성공했을 경우 true 반환
    }
}