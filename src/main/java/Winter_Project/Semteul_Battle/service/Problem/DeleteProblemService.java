package Winter_Project.Semteul_Battle.service.Problem;

import Winter_Project.Semteul_Battle.dto.Problem.DeleteProblemDto;
import Winter_Project.Semteul_Battle.repository.ContestRepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProblemService {
    private final ProblemRepository problemRepository;

    public void deleteProblem(DeleteProblemDto deleteProblemDto) {
        Long problemId = deleteProblemDto.getProblemId();

        problemRepository.deleteById(problemId);
    }
}
