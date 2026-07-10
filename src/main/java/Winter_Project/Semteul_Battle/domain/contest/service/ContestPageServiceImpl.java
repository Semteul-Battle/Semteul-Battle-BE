package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.contest.entity.Examiner;
import Winter_Project.Semteul_Battle.domain.contest.dto.response.ContestPageDto;
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
        ContestPageDto contestDTO = new ContestPageDto();
        contestDTO.setContests(contestPage.getContent());
        contestDTO.setExaminerIds(examinerIds); // Examiner ID ?饔낅떽????ш낄?뉔뇡?꾩땡沃섏쥓??????嚥싲갭큔???
        // ?????諛몃마??????癰궽블뀮??ш퀚?????? ?癲????????嚥싲갭큔??? ?????諛몃마??????癰궽블뀮??ш퀚?????? ?????嚥싲갭큔??? ?????諛몃마???????????嚥싲갭큔???        contestDTO.setCurrentPage(contestPage.getNumber());
        contestDTO.setTotalPages(contestPage.getTotalPages());
        contestDTO.setTotalItems(contestPage.getTotalElements());

        // ??????⑤뜪??????癰궽블뀮??ш퀚???????? ???嚥싲갭큔???????癰궽블뀮??ш퀚??????????轅붽틓?????????????댄뱼?????꿔꺂??????
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

    // ??????????????汝뷴젆??녷뉩??읂?Examiner??ID ?饔낅떽????ш낄?뉔뇡?꾩땡沃섏쥓???????ル늉????轅붽틓????筌뤾쑴裕?棺堉?뙴???
public List<Long> getExaminerIdsByContestId(Long contestId) {
        return examinerRepository.findByContest_Id(contestId).stream()
                .map(Examiner::getId)
                .collect(Collectors.toList());
    }
}