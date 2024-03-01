package Winter_Project.Semteul_Battle.service.Contest;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Examiner;
import Winter_Project.Semteul_Battle.dto.Contest.ContestPageDto;
import Winter_Project.Semteul_Battle.repository.ExaminerRepository;
import org.springframework.data.domain.Page;

import Winter_Project.Semteul_Battle.repository.ContestRepository;
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
public class ContestPageService {
    private final ContestRepository contestRepository;
    private final ExaminerRepository examinerRepository;

    public ContestPageDto buildDTO(Page<Contest> contestPage, List<Long> examinerIds) {
        ContestPageDto contestDTO = new ContestPageDto();
        contestDTO.setContests(contestPage.getContent());
        contestDTO.setExaminerIds(examinerIds); // Examiner ID 목록 설정

        // 현재 페이지 번호 설정, 전체 페이지 수 설정, 전체 대회 수 설정
        contestDTO.setCurrentPage(contestPage.getNumber());
        contestDTO.setTotalPages(contestPage.getTotalPages());
        contestDTO.setTotalItems(contestPage.getTotalElements());

        // 이전 페이지와 다음 페이지의 정보를 추가합니다.
        if (contestPage.hasPrevious()) {
            contestDTO.setPrevPage(contestPage.getNumber() - 1);
        }

        if (contestPage.hasNext()) {
            contestDTO.setNextPage(contestPage.getNumber() + 1);
        }

        return contestDTO;
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

    // 대회에 해당하는 Examiner의 ID 목록 가져오기
    public List<Long> getExaminerIdsByContestId(Long contestId) {
        return examinerRepository.findByContest_Id(contestId).stream()
                .map(Examiner::getId)
                .collect(Collectors.toList());
    }
}