package Winter_Project.Semteul_Battle.domain.problem.service;

import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import Winter_Project.Semteul_Battle.domain.problem.dto.UpdateProblemDto;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateProblemService {
    private final ProblemRepository problemRepository;

    public boolean updateProblem(UpdateProblemDto updateProblemDto) {
        // ????癰궽블뀯??????嶺???傭?끆???嶺뚮?猷볠꽴?????⑥ル럯????
Optional<Problem> optionalProblem = problemRepository.findById(updateProblemDto.getProblemId());

        // ???嶺???傭?끆??????? ???怨쀫뮡??????汝뷴젆??녷뉩??읂?γ볥덆???? ??轅붽틓????????
if (optionalProblem.isPresent()) {
            Problem problem = optionalProblem.get();


            problem.setTitle(updateProblemDto.getTitle());
            problem.setContent(updateProblemDto.getContent());
            problem.setInput(updateProblemDto.getInput());
            problem.setOutput(updateProblemDto.getOutput());
            problem.setTimeLimit(updateProblemDto.getTimeLimit());
            problem.setScore(updateProblemDto.getScore());

            // ???????살숲??????밸븶???????嶺???傭?끆???嶺뚮?猷볠꽴?????
problemRepository.save(problem);
return true;
        } else {
            return false; // ???嶺???傭?끆??????? ???怨쀫뮡?????? ??????猿딅빟 ??棺堉?뤃????
}
    }
}