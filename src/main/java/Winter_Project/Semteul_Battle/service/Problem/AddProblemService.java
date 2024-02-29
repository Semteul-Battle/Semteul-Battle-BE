package Winter_Project.Semteul_Battle.service.Problem;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Problem;
import Winter_Project.Semteul_Battle.dto.Problem.AddProblemDto;
import Winter_Project.Semteul_Battle.repository.ContestRepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class AddProblemService {
    private final ProblemRepository problemRepository;
    private final ContestRepository contestRepository;

    @Value("${file.basePath}")
    private String basePath; // 기본 경로 설정

    public void problemFrame(AddProblemDto addProblemDto) {
        // Contest ID를 사용하여 Contest 객체 찾기
        Contest contest = contestRepository.findById(addProblemDto.getContestId())
                .orElseThrow(() -> new RuntimeException("해당 대회를 찾을 수 없습니다."));

        // AddProblemDto를 Problem 엔티티로 변환하고 Contest 객체 설정
        Problem problem = Problem.builder()
                .number(addProblemDto.getNumber())
                .title(addProblemDto.getTitle())
                .content(addProblemDto.getContent())
                .input(addProblemDto.getInput())
                .output(addProblemDto.getOutput())
                .timeLimit(addProblemDto.getTimeLimit())
                .score(addProblemDto.getScore())
                .contest(contest) // Contest 객체 설정
                .build();

        // Problem 엔티티 저장
        problemRepository.save(problem);
    }



    // 입력 및 출력 파일 생성 메서드
    public void createInputOutputFiles(String inputContent, String outputContent, String problemNumber) {
        String inputFilePath = basePath + "\\input"; // 입력 파일 경로 설정
        String outputFilePath = basePath + "\\output"; // 출력 파일 경로 설정

        try {
            // 입력 파일 생성
            Files.createDirectories(Paths.get(inputFilePath, problemNumber)); // 입력 파일 디렉토리 생성
            int nextInputFileNumber = getNextFileNumber(inputFilePath + "\\" + problemNumber); // 다음 파일 번호 확인
            String inputFileName = inputFilePath + "\\" + problemNumber + "\\" + nextInputFileNumber + ".txt"; // 입력 파일 이름 설정
            Files.write(Paths.get(inputFileName), inputContent.getBytes());

            // 출력 파일 생성
            Files.createDirectories(Paths.get(outputFilePath, problemNumber)); // 출력 파일 디렉토리 생성
            int nextOutputFileNumber = getNextFileNumber(outputFilePath + "\\" + problemNumber); // 다음 파일 번호 확인
            String outputFileName = outputFilePath + "\\" + problemNumber + "\\" + nextOutputFileNumber + ".txt"; // 출력 파일 이름 설정
            Files.write(Paths.get(outputFileName), outputContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 생성 실패 처리
        }
    }

    // 다음 파일 번호 확인 메서드
    private int getNextFileNumber(String directoryPath) throws IOException {
        int nextFileNumber = 1;
        // 디렉토리 내 파일 목록 가져오기
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                nextFileNumber = files.length + 1; // 다음 파일 번호는 현재 파일 개수 + 1
            }
        }
        return nextFileNumber;
    }
}