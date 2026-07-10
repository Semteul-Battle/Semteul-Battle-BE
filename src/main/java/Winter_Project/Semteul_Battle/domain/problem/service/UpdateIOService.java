package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.entity.IO;
import Winter_Project.Semteul_Battle.domain.problem.dto.UpdateProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.repository.IORepository;
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