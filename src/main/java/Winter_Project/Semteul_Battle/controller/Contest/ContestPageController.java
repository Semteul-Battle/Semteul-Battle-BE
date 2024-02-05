package Winter_Project.Semteul_Battle.controller.Contest;


import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.dto.Contest.ContestPageDto;
import Winter_Project.Semteul_Battle.service.Contest.ContestPageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return buildDTO(contestPage);
    }

    // 진행중인 대회 목록 조회
    @GetMapping("/ongoing")
    public ContestPageDto getOngoingContests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Contest> contestPage = contestPageService.getOngoingContests(page, size);
        return buildDTO(contestPage);
    }

    // 예정인 대회 목록 조회
    @GetMapping("/scheduled")
    public ContestPageDto getScheduledContests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Contest> contestPage = contestPageService.getScheduledContests(page, size);
        return buildDTO(contestPage);
    }

    // 종료된 대회 목록 조회
    @GetMapping("/finished")
    public ContestPageDto getFinishedContests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Contest> contestPage = contestPageService.getFinishedContests(page, size);
        return buildDTO(contestPage);
    }

    private ContestPageDto buildDTO(Page<Contest> contestPage) {
        ContestPageDto contestDTO = new ContestPageDto();
        // 현재 페이지에 있는 대회 목록을 ContestPageDto에 설정
        contestDTO.setContests(contestPage.getContent());
        // 현재 페이지 번호 설정
        contestDTO.setCurrentPage(contestPage.getNumber());
        // 전체 페이지 수 설정
        contestDTO.setTotalPages(contestPage.getTotalPages());
        // 전체 대회 수 설정
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
}
