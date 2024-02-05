package Winter_Project.Semteul_Battle.service.Contest;

import Winter_Project.Semteul_Battle.domain.Contest;
import org.springframework.data.domain.Page;

import Winter_Project.Semteul_Battle.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class ContestPageService {

    private final ContestRepository contestRepository;

    // 메인 페이지 모든 상태의 대회 조회 (최신 순으로 정렬)
    public Page<Contest> getTotalContests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        return contestRepository.findAll(pageable);
    }

    // 진행 중인 대회 목록 조회 (최신 순으로 정렬)
    public Page<Contest> getOngoingContests(int page, int size) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        return contestRepository.findByStartTimeBeforeAndEndTimeAfter(currentTime, currentTime, pageable);
    }

    // 예정된 대회 목록 조회 (최신 순으로 정렬)
    public Page<Contest> getScheduledContests(int page, int size) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startTime"));
        return contestRepository.findByStartTimeAfter(currentTime, pageable);
    }

    // 종료된 대회 목록 조회 (최신 순으로 정렬)
    public Page<Contest> getFinishedContests(int page, int size) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endTime"));
        return contestRepository.findByEndTimeBefore(currentTime, pageable);
    }
}