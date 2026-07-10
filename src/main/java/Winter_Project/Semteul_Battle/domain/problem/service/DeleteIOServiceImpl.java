package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.dto.request.DeleteProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.repository.IORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteIOServiceImpl implements DeleteIOService {
    private final IORepository ioRepository;

    public void deleteIO(DeleteProblemDto deleteProblemDto) {
        Long ioId = deleteProblemDto.getIoId();

        ioRepository.deleteById(ioId);
    }
}
