package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.dto.request.DeleteProblemDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;

public interface DeleteProblemService {
    public void deleteProblem(DeleteProblemDto deleteProblemDto);
}
