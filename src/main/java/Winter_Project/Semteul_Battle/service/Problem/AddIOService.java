package Winter_Project.Semteul_Battle.service.Problem;

import Winter_Project.Semteul_Battle.domain.IO;
import Winter_Project.Semteul_Battle.domain.Problem;
import Winter_Project.Semteul_Battle.dto.Problem.AddIODto;
import Winter_Project.Semteul_Battle.repository.IORepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddIOService {
    private final IORepository ioRepository;
    private final ProblemRepository problemRepository;

    public void IOFrame(AddIODto addIODto) {
        Problem problem = problemRepository.findById(addIODto.getProblemId())
                .orElseThrow(() -> new RuntimeException("해당 문제를 찾을 수 없습니다."));

        IO io = IO.builder()
                .input(addIODto.getInput())
                .output(addIODto.getOutput())
                .problem(problem)
                .build();

        ioRepository.save(io);
    }
}
