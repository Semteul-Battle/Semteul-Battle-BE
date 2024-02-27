package Winter_Project.Semteul_Battle.service.Problem;

import Winter_Project.Semteul_Battle.domain.Problem;
import Winter_Project.Semteul_Battle.dto.Problem.UpdateProblemDto;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateProblemService {
    private final ProblemRepository problemRepository;

    public boolean updateProblem(UpdateProblemDto updateProblemDto) {
        // 수정할 문제를 불러옴
        Optional<Problem> optionalProblem = problemRepository.findById(updateProblemDto.getProblemId());

        // 문제가 존재하는지 확인
        if (optionalProblem.isPresent()) {
            Problem problem = optionalProblem.get();


            problem.setTitle(updateProblemDto.getTitle());
            problem.setContent(updateProblemDto.getContent());
            problem.setInput(updateProblemDto.getInput());
            problem.setOutput(updateProblemDto.getOutput());
            problem.setTimeLimit(updateProblemDto.getTimeLimit());
            problem.setScore(updateProblemDto.getScore());

            // 업데이트된 문제를 저장
            problemRepository.save(problem);
            return true;
        } else {
            return false; // 문제가 존재하지 않는 경우
        }
    }
}