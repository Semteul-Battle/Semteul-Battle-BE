package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.entity.IO;
import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import Winter_Project.Semteul_Battle.domain.problem.dto.AddIODto;
import Winter_Project.Semteul_Battle.domain.problem.repository.IORepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddIOService {
    private final IORepository ioRepository;
    private final ProblemRepository problemRepository;

    public void IOFrame(AddIODto addIODto) {
        Problem problem = problemRepository.findById(addIODto.getProblemId())
                .orElseThrow(() -> new RuntimeException("????????嶺???傭?끆???嶺뚮?猷볠꽴??饔낅떽???????????????깅즽????????놁졄."));

        IO io = IO.builder()
                .input(addIODto.getInput())
                .output(addIODto.getOutput())
                .problem(problem)
                .build();

        ioRepository.save(io);
    }
}
