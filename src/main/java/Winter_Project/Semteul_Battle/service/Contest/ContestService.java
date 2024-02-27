package Winter_Project.Semteul_Battle.service.Contest;

import Winter_Project.Semteul_Battle.domain.*;
import Winter_Project.Semteul_Battle.dto.Contest.ContestInfoDTO;
import Winter_Project.Semteul_Battle.dto.Contest.CreateContestDto;
import Winter_Project.Semteul_Battle.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collections;
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
    private final ProblemRepository problemRepository;
    private final IORepository ioRepository;

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
        // 대회에 속한 모든 문제를 찾음
        List<Problem> problems = problemRepository.findByContest_Id(contestId);

        // 모든 문제에 대응하는 입출력 예시를 삭제
        for (Problem problem : problems) {
            ioRepository.deleteByProblem_Id(problem.getId());
        }

        // 대회와 연관된 모든 정보를 삭제
        problemRepository.deleteByContest_Id(contestId);
        examinerRepository.deleteByContest_Id(contestId);
        contestRepository.deleteById(contestId);
    }



    // 대회 아이디로 문제 불러오기
//    public List<Problem> getProblemsByContestId(Long contestId) {
//        return problemRepository.findByContest_Id(contestId);
//    }

//    public ContestInfoDTO getContestInfo(Long contestId, String loginId) {
//        ContestInfoDTO contestInfoDTO = new ContestInfoDTO();
//
//        // 참가자 상태 확인
//        Long participantStatus = whoAreU(contestId, loginId);
//        contestInfoDTO.setParticipantStatus(participantStatus);
//
//        // 문제 정보 가져오기
//        List<Problem> problems = problemRepository.findByContest_Id(contestId);
//        contestInfoDTO.setProblems(problems != null ? problems : Collections.emptyList());
//
//        // 대회 공지 가져오기
//        List<ContestNotice> notices = contestNoticeRepository.findByContest_Id(contestId);
//        contestInfoDTO.setNotices(notices != null ? notices : Collections.emptyList());
//
//        return contestInfoDTO;
//    }

    @Transactional(readOnly = false)
    public Contest getContestById(Long contestId) {
        return contestRepository.findById(contestId).orElse(null);
    }



}
