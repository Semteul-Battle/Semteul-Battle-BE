package Winter_Project.Semteul_Battle.service.Problem;

import Winter_Project.Semteul_Battle.domain.Problem;
import Winter_Project.Semteul_Battle.dto.Problem.AddProblemDto;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddProblemService {
    private final ProblemRepository problemRepository;

    public void problemFrame(AddProblemDto addProblemDto) {
        // AddProblemDto를 Problem 엔티티로 변환
        Problem problem = Problem.builder()
                .number(addProblemDto.getNumber())
                .title(addProblemDto.getTitle())
                .content(addProblemDto.getContent())
                .input(addProblemDto.getInput())
                .output(addProblemDto.getOutput())
                .timeLimit(addProblemDto.getTimeLimit())
                .score(addProblemDto.getScore())
                .pic(addProblemDto.getPic())
                .build();

        // Problem 엔티티 저장
        problemRepository.save(problem);
    }
}
