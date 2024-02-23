package Winter_Project.Semteul_Battle.service.Problem;

import Winter_Project.Semteul_Battle.dto.Problem.DeleteProblemDto;
import Winter_Project.Semteul_Battle.repository.IORepository;
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
