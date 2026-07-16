package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.dto.request.ContestApplicationDto;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contestant;
import Winter_Project.Semteul_Battle.domain.contest.entity.ContestantContest;
import Winter_Project.Semteul_Battle.domain.contest.exception.ContestException;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestantContestRepository;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestantRepository;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContestApplicationServiceImpl implements ContestApplicationService {
    private final ContestRepository contestRepository;
    private final ContestantRepository contestantRepository;
    private final ContestantContestRepository contestantContestRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean applyContest(ContestApplicationDto contestApplicationDto, String loginId) {
        Users user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + loginId));
        Contest contest = contestRepository.findByContestName(contestApplicationDto.getContestName())
                .orElseThrow(() -> new ContestException(ErrorStatus._NOT_FOUND, "신청 대상 대회를 찾을 수 없습니다."));

        Contestant contestant = new Contestant();
        contestant.assignUser(user);
        Contestant savedContestant = contestantRepository.save(contestant);

        ContestantContest contestantContest = new ContestantContest();
        contestantContest.assignContest(contest);
        contestantContest.assignContestant(savedContestant);
        contestantContestRepository.save(contestantContest);

        return true;
    }
}
