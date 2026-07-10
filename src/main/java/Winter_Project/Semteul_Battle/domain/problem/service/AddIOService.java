package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.entity.IO;
import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.AddIODto;
import Winter_Project.Semteul_Battle.domain.problem.repository.IORepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;

public interface AddIOService {
    public void IOFrame(AddIODto addIODto);
}
