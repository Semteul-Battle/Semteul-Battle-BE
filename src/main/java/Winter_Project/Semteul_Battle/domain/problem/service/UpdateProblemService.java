package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.UpdateProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;

import java.util.Optional;

public interface UpdateProblemService {
    public boolean updateProblem(UpdateProblemDto updateProblemDto);
}
