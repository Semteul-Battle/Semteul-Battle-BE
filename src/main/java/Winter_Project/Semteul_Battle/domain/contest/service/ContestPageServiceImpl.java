package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.Examiner;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestPageDto;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestSummaryDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.ExaminerRepository;
import org.springframework.data.domain.Page;

import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestPageServiceImpl implements ContestPageService {
    private final ContestRepository contestRepository;
    private final ExaminerRepository examinerRepository;

    public ContestPageDto buildDTO(Page<Contest> contestPage, List<Long> examinerIds) {
        List<ContestSummaryDto> contests = contestPage.getContent().stream()
                .map(ContestSummaryDto::from)
                .collect(Collectors.toList());
        Integer prevPage = contestPage.hasPrevious() ? contestPage.getNumber() - 1 : null;
        Integer nextPage = contestPage.hasNext() ? contestPage.getNumber() + 1 : null;

        return ContestPageDto.of(
                contests,
                examinerIds,
                contestPage.getNumber(),
                contestPage.getTotalPages(),
                contestPage.getTotalElements(),
                prevPage,
                nextPage
        );
    }



    public Page<Contest> getTotalContests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        return contestRepository.findAll(pageable);
    }

    public Page<Contest> getOngoingContests(int page, int size) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        return contestRepository.findByStartTimeBeforeAndEndTimeAfter(currentTime, currentTime, pageable);
    }

    public Page<Contest> getScheduledContests(int page, int size) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        return contestRepository.findByStartTimeAfter(currentTime, pageable);
    }

    public Page<Contest> getFinishedContests(int page, int size) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endTime"));
        return contestRepository.findByEndTimeBefore(currentTime, pageable);
    }


public List<Long> getExaminerIdsByContestId(Long contestId) {
        return examinerRepository.findByContest_Id(contestId).stream()
                .map(Examiner::getId)
                .collect(Collectors.toList());
    }
}
