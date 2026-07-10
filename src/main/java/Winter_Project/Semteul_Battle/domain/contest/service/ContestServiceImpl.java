package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.user.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestInfoDTO;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.CreateContestDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.*;
import Winter_Project.Semteul_Battle.domain.problem.repository.*;
import Winter_Project.Semteul_Battle.domain.user.repository.*;
import Winter_Project.Semteul_Battle.domain.menu.repository.*;
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
public class ContestServiceImpl implements ContestService {

    private final ContestRepository contestRepository;
    private final ExaminerRepository examinerRepository;
    private final ProblemRepository problemRepository;
    private final IORepository ioRepository;
    private final ContestNoticeRepository contestNoticeRepository;
    private final ContestLiveService contestLiveService;


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


@Transactional
    public void deleteContest(Long contestId) {

List<Problem> problems = problemRepository.findByContest_Id(contestId);


for (Problem problem : problems) {
            ioRepository.deleteByProblem_Id(problem.getId());
        }


problemRepository.deleteByContest_Id(contestId);
        examinerRepository.deleteByContest_Id(contestId);
        contestRepository.deleteById(contestId);
    }




public List<Problem> getProblemsByContestId(Long contestId) {
//
return problemRepository.findByContest_Id(contestId);
//
}

//
public ContestInfoDTO getContestInfo(Long contestId, String loginId) {
//
ContestInfoDTO contestInfoDTO = new ContestInfoDTO();
//

Long participantStatus = contestLiveService.whoAreU(contestId, loginId);
//        contestInfoDTO.setParticipantStatus(participantStatus);
//

List<Problem> problems = problemRepository.findByContest_Id(contestId);
//        contestInfoDTO.setProblems(problems != null ? problems : Collections.emptyList());
//

List<ContestNotice> notices = contestNoticeRepository.findByContest_Id(contestId);
//        contestInfoDTO.setNotices(notices != null ? notices : Collections.emptyList());
//
//
return contestInfoDTO;
//
}
@Transactional(readOnly = false)
    public Contest getContestById(Long contestId) {
        return contestRepository.findById(contestId).orElse(null);
    }



}
