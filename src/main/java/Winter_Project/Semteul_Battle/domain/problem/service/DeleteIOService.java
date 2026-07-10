package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.dto.request.DeleteProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.repository.IORepository;

public interface DeleteIOService {
    public void deleteIO(DeleteProblemDto deleteProblemDto);
}
