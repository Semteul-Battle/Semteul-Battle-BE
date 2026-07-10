package Winter_Project.Semteul_Battle.domain.contest.service;

import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import Winter_Project.Semteul_Battle.domain.contest.entity.Submit;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.contest.dto.request.SubmitDTO;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;
import Winter_Project.Semteul_Battle.domain.contest.repository.SubmitRepository;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SubmitServiceImpl implements SubmitService {

    private final SubmitRepository submitRepository;
    private final ContestRepository contestRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;



public synchronized Long saveSubmit(SubmitDTO submitDTO) {

String language = submitDTO.getLanguage();
Long runtime = submitDTO.getRuntime();
        Long memoLimit = submitDTO.getMemoLimit();
        String code = submitDTO.getCode();
        Long result = submitDTO.getResult();
        Timestamp time = submitDTO.getTime();
        Long problemId = submitDTO.getProblem().getId();
        Long contestId = submitDTO.getContestId();
Long userId = submitDTO.getUserId();

Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException("Problem with id " + problemId + " not found"));


Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new EntityNotFoundException("Contest with id " + contestId + " not found"));


Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));


Submit submit = Submit.builder()
                .language(language)
                .runtime(runtime)
                .memoLimit(memoLimit)
                .code(code)
                .result(result)
                .time(time)
                .problem(problem)
                .contest(contest)
.users(user)
.build();


Submit savedSubmit = submitRepository.save(submit);


return savedSubmit.getId();
    }





public synchronized void saveCodeToFile(SubmitDTO submitDTO, Long submitId) {

String fileName = "init.txt";


        // /Users/hwangs/project/compile/code
        try (FileWriter fileWriter = new FileWriter("/Users/hwangs/project/compile/code/" + fileName)) {

int language = 0;
            String temp;
            if ((temp = submitDTO.getLanguage()).equals("c"))
                language = 0;
            else if (temp.equals("cpp"))
                language = 1;
            else if (temp.equals("java"))
                language = 2;
            else if (temp.equals("python"))
                language = 3;

            System.out.println("Unsupported language: " + language);

            fileWriter.write(language + "\n");
} catch (IOException e) {
            e.printStackTrace();

}




}



public synchronized void runGradingProgram(String programPath, Long submitId) {
        try {

ProcessBuilder processBuilder = new ProcessBuilder(programPath);
            processBuilder.directory(new File("/Users/hwangs/project/compile/code/"));
            Process process = processBuilder.start();

            System.out.println("debug");


} catch (Exception e) {
            System.out.println("grading completed");

            e.printStackTrace();

}
    }



private synchronized void returnProgramResult(String resultPath, Long submitId) {
        try {
            System.out.println("read grading result");
            String resultFileName = "result_" + submitId +".txt";


String result = new String(Files.readAllBytes(Path.of(resultPath, resultFileName)));

            System.out.println("result = " + result);


Optional<Submit> optionalSubmit = submitRepository.findById(submitId);

            if (optionalSubmit.isPresent()) {

Submit submit = optionalSubmit.get();




} else {

}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
