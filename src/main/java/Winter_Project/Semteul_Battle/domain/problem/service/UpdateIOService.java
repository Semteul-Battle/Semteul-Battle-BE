package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.entity.IO;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.UpdateProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.repository.IORepository;

import java.util.Optional;

public interface UpdateIOService {
    public boolean UpdateIO(UpdateProblemDto updateProblemDto);
}
