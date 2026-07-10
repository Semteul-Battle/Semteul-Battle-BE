package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.dto.DeleteProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.repository.IORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteIOService {
    private final IORepository ioRepository;

    public void deleteIO(DeleteProblemDto deleteProblemDto) {
        Long ioId = deleteProblemDto.getIoId();

        ioRepository.deleteById(ioId);
    }
}
