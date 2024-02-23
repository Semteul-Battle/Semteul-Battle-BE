package Winter_Project.Semteul_Battle.service.Problem;

import Winter_Project.Semteul_Battle.domain.IO;
import Winter_Project.Semteul_Battle.dto.Problem.UpdateProblemDto;
import Winter_Project.Semteul_Battle.repository.IORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateIOService {
    private final IORepository ioRepository;

    public boolean UpdateIO(UpdateProblemDto updateProblemDto) {
        Optional<IO> optionalIO = ioRepository.findById(updateProblemDto.getIoId());

        if (optionalIO.isPresent()) {
            IO io = optionalIO.get();

            io.setInput(updateProblemDto.getInputIO());
            io.setOutput(updateProblemDto.getOutputIO());

            ioRepository.save(io);
            return true;
        } else {
            return false;
        }
    }
}