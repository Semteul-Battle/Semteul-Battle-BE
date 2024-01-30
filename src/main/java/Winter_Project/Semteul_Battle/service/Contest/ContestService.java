package Winter_Project.Semteul_Battle.service.Contest;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.dto.Contest.CreateContestDto;
import Winter_Project.Semteul_Battle.repository.ContestRepository;
import Winter_Project.Semteul_Battle.repository.ExaminerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ContestService {

    private final ContestRepository contestRepository;
    private final ExaminerRepository examinerRepository;

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

}
