package Winter_Project.Semteul_Battle.service.Problem;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Problem;
import Winter_Project.Semteul_Battle.dto.Problem.AddProblemDto;
import Winter_Project.Semteul_Battle.repository.ContestRepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddProblemService {
    private final ProblemRepository problemRepository;
    private final ContestRepository contestRepository;

    public void problemFrame(AddProblemDto addProblemDto) {
        // Contest ID를 사용하여 Contest 객체 찾기
        Contest contest = contestRepository.findById(addProblemDto.getContestId())
                .orElseThrow(() -> new RuntimeException("Contest not found"));

        // AddProblemDto를 Problem 엔티티로 변환하고 Contest 객체 설정
        Problem problem = Problem.builder()
                .number(addProblemDto.getNumber())
                .title(addProblemDto.getTitle())
                .content(addProblemDto.getContent())
                .input(addProblemDto.getInput())
                .output(addProblemDto.getOutput())
                .timeLimit(addProblemDto.getTimeLimit())
                .score(addProblemDto.getScore())
                .pic(addProblemDto.getPic())
                .contest(contest) // Contest 객체 설정
                .build();

        // Problem 엔티티 저장
        problemRepository.save(problem);
    }
}
