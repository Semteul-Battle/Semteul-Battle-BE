package Winter_Project.Semteul_Battle.service.Problem;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Problem;
import Winter_Project.Semteul_Battle.dto.Problem.AddProblemDto;
import Winter_Project.Semteul_Battle.repository.ContestRepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class AddProblemService {
    private final ProblemRepository problemRepository;
    private final ContestRepository contestRepository;

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
                .pic(addProblemDto.getPic())
                .contest(contest) // Contest 객체 설정
                .build();

        // Problem 엔티티 저장
        problemRepository.save(problem);
    }

    // 입력 및 출력 파일 생성 메서드
    public void createInputOutputFiles(String inputContent, String outputContent) {
        String basePath = "C:\\Users\\82104\\Desktop\\project"; // 기본 경로 설정
        String inputFileName = basePath + "\\input.txt"; // 입력 파일 이름 설정
        String outputFileName = basePath + "\\output.txt"; // 출력 파일 이름 설정

        try {
            // 입력 파일 생성
            Files.write(Paths.get(inputFileName), inputContent.getBytes());
            // 출력 파일 생성
            Files.write(Paths.get(outputFileName), outputContent.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            // 파일 생성 실패 처리
        }
    }
}
