package Winter_Project.Semteul_Battle.domain.problem.service;



import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.domain.problem.exception.ProblemException;
import Winter_Project.Semteul_Battle.domain.contest.entity.Contest;
import Winter_Project.Semteul_Battle.domain.problem.entity.Problem;
import Winter_Project.Semteul_Battle.domain.problem.dto.request.AddProblemDto;
import Winter_Project.Semteul_Battle.domain.contest.repository.ContestRepository;
import Winter_Project.Semteul_Battle.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class AddProblemServiceImpl implements AddProblemService {
    private final ProblemRepository problemRepository;
    private final ContestRepository contestRepository;

    @Value("${file.basePath}")
    private String basePath;
public void problemFrame(AddProblemDto addProblemDto) {

Contest contest = contestRepository.findById(addProblemDto.getContestId())
                .orElseThrow(() -> new ProblemException(ErrorStatus._NOT_FOUND, "대회를 찾을 수 없습니다."));


Problem problem = Problem.builder()
                .number(addProblemDto.getNumber())
                .title(addProblemDto.getTitle())
                .content(addProblemDto.getContent())
                .input(addProblemDto.getInput())
                .output(addProblemDto.getOutput())
                .timeLimit(addProblemDto.getTimeLimit())
                .score(addProblemDto.getScore())
                .contest(contest)
.build();


problemRepository.save(problem);
    }




public void createInputOutputFiles(String inputContent, String outputContent, String problemNumber) {
        String inputFilePath = basePath + "\\input";
String outputFilePath = basePath + "\\output";
try {

int nextInputFileNumber = getNextFileNumber(inputFilePath + "\\" + problemNumber);
String inputFileName = inputFilePath + "\\" + problemNumber + "\\" + nextInputFileNumber + ".txt";


int nextOutputFileNumber = getNextFileNumber(outputFilePath + "\\" + problemNumber);
String outputFileName = outputFilePath + "\\" + problemNumber + "\\" + nextOutputFileNumber + ".txt";
        } catch (IOException e) {
            e.printStackTrace();

}
    }


private int getNextFileNumber(String directoryPath) throws IOException {
        int nextFileNumber = 1;

File directory = new File(directoryPath);
if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                nextFileNumber = files.length + 1;
}
        }
        return nextFileNumber;
    }
}
