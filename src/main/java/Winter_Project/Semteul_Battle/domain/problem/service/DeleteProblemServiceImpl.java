package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.dto.request.DeleteProblemDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProblemServiceImpl implements DeleteProblemService {
    private final ProblemRepository problemRepository;

    public void deleteProblem(DeleteProblemDto deleteProblemDto) {
        Long problemId = deleteProblemDto.getProblemId();

        problemRepository.deleteById(problemId);
    }
}
