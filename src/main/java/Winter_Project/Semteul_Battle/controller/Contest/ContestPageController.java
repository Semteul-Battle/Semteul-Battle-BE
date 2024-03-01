package Winter_Project.Semteul_Battle.controller.Contest;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Examiner;
import Winter_Project.Semteul_Battle.dto.Contest.ContestPageDto;
import Winter_Project.Semteul_Battle.repository.ExaminerRepository;
import Winter_Project.Semteul_Battle.service.Contest.ContestPageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/contests")
public class ContestPageController {
    private final ContestPageService contestPageService;

    // 모든 대회 목록 조회
    @GetMapping("/all")
    public ContestPageDto getTotalContests(
            @RequestParam(defaultValue = "0", value = "page") int page,
            @RequestParam(defaultValue = "10", value = "size") int size) {
        Page<Contest> contestPage = contestPageService.getTotalContests(page, size);
        List<Long> examinerIds = contestPage.getContent().stream()
                .map(Contest::getId)
                .map(contestPageService::getExaminerIdsByContestId)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return contestPageService.buildDTO(contestPage, examinerIds);
    }

    // 진행중인 대회 목록 조회
    @GetMapping("/ongoing")
    public ContestPageDto getOngoingContests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Contest> contestPage = contestPageService.getOngoingContests(page, size);
        List<Long> examinerIds = contestPage.getContent().stream()
                .map(Contest::getId)
                .map(contestPageService::getExaminerIdsByContestId)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return contestPageService.buildDTO(contestPage, examinerIds);
    }

    // 예정인 대회 목록 조회
    @GetMapping("/scheduled")
    public ContestPageDto getScheduledContests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Contest> contestPage = contestPageService.getScheduledContests(page, size);
        List<Long> examinerIds = contestPage.getContent().stream()
                .map(Contest::getId)
                .map(contestPageService::getExaminerIdsByContestId)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return contestPageService.buildDTO(contestPage, examinerIds);
    }

    // 종료된 대회 목록 조회
    @GetMapping("/finished")
    public ContestPageDto getFinishedContests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Contest> contestPage = contestPageService.getFinishedContests(page, size);
        List<Long> examinerIds = contestPage.getContent().stream()
                .map(Contest::getId)
                .map(contestPageService::getExaminerIdsByContestId)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return contestPageService.buildDTO(contestPage, examinerIds);
    }
}