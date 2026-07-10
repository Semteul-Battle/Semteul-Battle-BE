package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.Examiner;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestPageDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.ExaminerRepository;
import org.springframework.data.domain.Page;

import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public interface ContestPageService {
    public ContestPageDto buildDTO(Page<Contest> contestPage, List<Long> examinerIds);
    public Page<Contest> getTotalContests(int page, int size);
    public Page<Contest> getOngoingContests(int page, int size);
    public Page<Contest> getScheduledContests(int page, int size);
    public Page<Contest> getFinishedContests(int page, int size);
    public List<Long> getExaminerIdsByContestId(Long contestId);
}
