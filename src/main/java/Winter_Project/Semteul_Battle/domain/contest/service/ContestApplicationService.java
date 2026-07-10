package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contestant;
import Winter_Project.Semteul_Battle.domain.contest.entity.ContestantContest;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.contest.dto.ContestApplicationDto;
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
public class ContestApplicationService {
    private final ContestRepository contestRepository;
    private final ContestantRepository contestantRepository;
    private final ContestantContestRepository contestantContestRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public boolean applyContest(ContestApplicationDto contestApplicationDto, String token) {

        // Contestant ????ル늉?????????熬곣뫖利?????꿔꺂?????????????轅붽틓??????????嚥싲갭큔???
String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUser = userRepository.findByLoginId(loginId);
        // Contestant ???????????熬곣뫖利???
Contestant contestant = new Contestant();
        contestant.setUsers(optionalUser.get());

        // ?????????????ル늉????轅붽틓????筌뤾쑴裕?棺堉?뙴???
Optional<Contest> ContestName = contestRepository.findByContestName(contestApplicationDto.getContestName());
        if (ContestName.isEmpty()) {
            log.error("??????轅붽틓?????????饔낅떽???????????????깅즽????????놁졄.");
            return false;
        }
        Contest contest = ContestName.get();
        Contestant savedContestant = contestantRepository.save(contestant);

//         ????????饔낅떽????怨뚮옩??????????? ??????轅붽틓????????ContestantContest ???????????????????
ContestantContest contestantContest = new ContestantContest();
        contestantContest.setContest(contest);
        contestantContest.setContestant(savedContestant);

        // ContestantContest ????????????        contestantContestRepository.save(contestantContest);
return true; // ??????????읐?????關?쒎첎????곌램伊????嚥싲갭큔?????棺堉?뤃????true ????썹땟戮녹????
}
}