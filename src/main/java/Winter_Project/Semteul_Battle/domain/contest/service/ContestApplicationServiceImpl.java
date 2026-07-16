package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contestant;
import Winter_Project.Semteul_Battle.domain.contest.entity.ContestantContest;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestApplicationDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestantContestRepository;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestantRepository;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContestApplicationServiceImpl implements ContestApplicationService {
    private final ContestRepository contestRepository;
    private final ContestantRepository contestantRepository;
    private final ContestantContestRepository contestantContestRepository;
    private final UserRepository userRepository;

    public boolean applyContest(ContestApplicationDto contestApplicationDto, String loginId) {
        Optional<Users> optionalUser = userRepository.findByLoginId(loginId);

Contestant contestant = new Contestant();
        contestant.assignUser(optionalUser.get());


Optional<Contest> ContestName = contestRepository.findByContestName(contestApplicationDto.getContestName());
        if (ContestName.isEmpty()) {
            log.error("이미 대회에 신청한 사용자입니다.");
            return false;
        }
        Contest contest = ContestName.get();
        Contestant savedContestant = contestantRepository.save(contestant);


ContestantContest contestantContest = new ContestantContest();
        contestantContest.assignContest(contest);
        contestantContest.assignContestant(savedContestant);


return true;
}
}
