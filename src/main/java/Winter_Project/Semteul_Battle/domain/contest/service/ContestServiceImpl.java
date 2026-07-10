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

    // ???????熬곣뫖利???
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

    // ????????
@Transactional
    public void deleteContest(Long contestId) {
        // ?????????????깅즽???饔낅떽????ш낄?뉔뇡??????嶺???傭?끆???嶺뚮?猷볠꽴??饔낅떽???????
List<Problem> problems = problemRepository.findByContest_Id(contestId);

        // ?饔낅떽????ш낄?뉔뇡??????嶺???????????筌???????????멸눋?????????살퓢???????
for (Problem problem : problems) {
            ioRepository.deleteByProblem_Id(problem.getId());
        }

        // ????? ??????饔낅떽????ш낄?뉔뇡?????轅붽틓????????????
problemRepository.deleteByContest_Id(contestId);
        examinerRepository.deleteByContest_Id(contestId);
        contestRepository.deleteById(contestId);
    }



    // ?????????諛몃마??維◈????嫄?筌뚯궢?????嶺????????⑥ル럯???????⑥ロ꺘??//
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
//        // ?饔낅떽????怨뚮옩??????????됰Ŧ鍮????轅붽틓????????//
Long participantStatus = contestLiveService.whoAreU(contestId, loginId);
//        contestInfoDTO.setParticipantStatus(participantStatus);
//
//        // ???嶺??????轅붽틓???????????ル늉????轅붽틓????筌뤾쑴裕?棺堉?뙴???//
List<Problem> problems = problemRepository.findByContest_Id(contestId);
//        contestInfoDTO.setProblems(problems != null ? problems : Collections.emptyList());
//
//        // ?????????怨룹?? ????ル늉????轅붽틓????筌뤾쑴裕?棺堉?뙴???//
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
