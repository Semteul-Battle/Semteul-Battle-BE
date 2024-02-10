package Winter_Project.Semteul_Battle.service.Contest;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Contestant;
import Winter_Project.Semteul_Battle.domain.ContestantContest;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.Contest.CreateContestDto;
import Winter_Project.Semteul_Battle.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ContestService {

    private final ContestRepository contestRepository;
    private final ExaminerRepository examinerRepository;
    private final ContestantContestRepository contestantContestRepository;
    private final UserRepository userRepository;
    private final ContestantRepository contestantRepository;


    // 대회 생성
    @Transactional
    public Contest createContest(CreateContestDto createContestDto) {

        Contest newContest = Contest.builder()
                .contestName(createContestDto.getContestName())
                .enterAuthority(createContestDto.getEnterAuthority())
                .startTime(Timestamp.valueOf(createContestDto.getStartTime().toLocalDateTime()))
                .endTime(Timestamp.valueOf(createContestDto.getEndTime().toLocalDateTime()))
                .build();

        newContest = contestRepository.save(newContest);

        return newContest;
    }

    // 대회 삭제
    @Transactional
    public void deleteContest(Long contestId) {

        examinerRepository.deleteByContest_Id(contestId);
        contestRepository.deleteById(contestId);
    }

    // 토큰에서 추출한 아이디로 일반 유저인지 출제자인지 판단
    public Long whoAreU(Long contestId, String loginId) {

        // 출제자 인지 체크
        boolean isExaminer = examinerRepository.existsByUsers_LoginIdAndContest_Id(loginId, contestId);
        if (isExaminer)
            return Long.valueOf(0); // 출제자일 경우

        System.out.println("통과");

        // 참가자 인지 체크
        // ContestantContest 테이블에서 먼저 클라이언트한테 받아온 contest_id로 리스트 contestant_id 추출
        List<ContestantContest> contestantContests = contestantContestRepository.findContestantContestByContest_Id(contestId);
        if (contestantContests.isEmpty()) {
            return Long.valueOf(2); // 대회에 해당하는 참가자가 없는 경우
        }

        // contestantContests 리스트에서 contestant_id만 추출
        List<Long> contestantIds = contestantContests.stream()
                .map(contestantContest -> contestantContest.getContestant().getId())
                .collect(Collectors.toList());

        // 로그인 아이디로 유저 id 추출
        Optional<Users> userOptional = userRepository.findByLoginId(loginId);
        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();

            // contestantIds 리스트에 있는 contestant_id을 contestant 테이블에서, 위에서 뽑은 userId로 같은지 체크
            Optional<Contestant> contestantOptional = contestantRepository.findByIdInAndUsers_Id(contestantIds, userId);
            if (contestantOptional.isPresent()) {
                // 해당하는 사용자 ID를 가진 참가자가 존재할 경우
                return Long.valueOf(1); // 참가자일 경우
            } else {
                // 해당하는 사용자 ID를 가진 참가자가 존재하지 않을 경우
                return Long.valueOf(2); // 참가자가 아닐 경우
            }
        } else {
            // 사용자가 존재하지 않을 경우에 대한 처리
            return Long.valueOf(2);
        }
    }


}
