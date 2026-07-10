package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.UpdateProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateProblemServiceImpl implements UpdateProblemService {
    private final ProblemRepository problemRepository;

    public boolean updateProblem(UpdateProblemDto updateProblemDto) {

Optional<Problem> optionalProblem = problemRepository.findById(updateProblemDto.getProblemId());


if (optionalProblem.isPresent()) {
            Problem problem = optionalProblem.get();


            problem.setTitle(updateProblemDto.getTitle());
            problem.setContent(updateProblemDto.getContent());
            problem.setInput(updateProblemDto.getInput());
            problem.setOutput(updateProblemDto.getOutput());
            problem.setTimeLimit(updateProblemDto.getTimeLimit());
            problem.setScore(updateProblemDto.getScore());


problemRepository.save(problem);
return true;
        } else {
            return false;
}
    }
}
