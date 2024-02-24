package Winter_Project.Semteul_Battle.service.Contest;

import Winter_Project.Semteul_Battle.domain.Contest;
import Winter_Project.Semteul_Battle.domain.Problem;
import Winter_Project.Semteul_Battle.domain.Submit;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.Contest.SubmitDTO;
import Winter_Project.Semteul_Battle.repository.ContestRepository;
import Winter_Project.Semteul_Battle.repository.ProblemRepository;
import Winter_Project.Semteul_Battle.repository.SubmitRepository;
import Winter_Project.Semteul_Battle.repository.UserRepository;
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
public class SubmitService {

    private final SubmitRepository submitRepository;
    private final ContestRepository contestRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;


    // DB에 저장
    @jakarta.transaction.Transactional
    public Long saveSubmit(SubmitDTO submitDTO) {
        // SubmitDTO에서 필요한 정보 추출
        String language = submitDTO.getLanguage();
        Long runtime = submitDTO.getRuntime();
        Long memoLimit = submitDTO.getMemoLimit();
        String code = submitDTO.getCode();
        Long result = submitDTO.getResult();
        Timestamp time = submitDTO.getTime();
        Long problemId = submitDTO.getProblem().getId();
        Long contestId = submitDTO.getContestId(); // 대회 번호 가져오기
        Long userId = submitDTO.getUserId(); // 사용자 아이디 가져오기

        // Problem 엔티티를 찾아옴
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException("Problem with id " + problemId + " not found"));

        // Contest 엔티티를 찾아옴
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new EntityNotFoundException("Contest with id " + contestId + " not found"));

        // User 엔티티를 찾아옴
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        // 엔티티 생성
        Submit submit = Submit.builder()
                .language(language)
                .runtime(runtime)
                .memoLimit(memoLimit)
                .code(code)
                .result(result)
                .time(time)
                .problem(problem)
                .contest(contest) // Contest 엔티티 설정
                .users(user) // User 엔티티 설정
                .build();

        // 엔티티를 데이터베이스에 저장
        Submit savedSubmit = submitRepository.save(submit);

        // 저장된 엔터티의 ID를 반환
        return savedSubmit.getId();
    }




    // 파일 저장
    @jakarta.transaction.Transactional
    public synchronized void saveCodeToFile(SubmitDTO submitDTO, Long submitId) {
        // 특정 폴더에 파일 저장 로직 구현
        // 파일명은 problemId, language 등을 활용하여 생성
        String fileName = "init.txt";


        // /Users/hwangs/project/compile/code
        try (FileWriter fileWriter = new FileWriter("/Users/hwangs/project/compile/code/" + fileName)) {
            // 각 정보를 새로운 줄에 저장
            fileWriter.write(submitDTO.getId() + "\n"); // 문제의 고유 번호
            fileWriter.write(submitId + "\n"); // 제출 테이블의 기본키 번호

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

            System.out.println("여기가 바뀐겁니다. : " + language);

            fileWriter.write(language + "\n"); // 언어
            fileWriter.write(submitDTO.getRuntime() + "\n"); // 채점 실행시간
            fileWriter.write(submitDTO.getMemoLimit() + "\n"); // 메모리 제한
            fileWriter.write(submitDTO.getCode()); // 코드
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 저장 실패 처리
        }

        // 채점
        runGradingProgram("/Users/hwangs/project/compile/main", submitId);

        // 결과
        returnProgramResult("/Users/hwangs/project/result", submitId);
    }


    // 채점하기
    public synchronized void runGradingProgram(String programPath, Long submitId) {
        try {
            // 채점 프로그램 실행 로직
            ProcessBuilder processBuilder = new ProcessBuilder(programPath);
            processBuilder.directory(new File("/Users/hwangs/project/compile/code/"));
            Process process = processBuilder.start();

            System.out.println("채점했음");

            // 채점 프로그램이 실행되고 완료될 때까지 대기
            process.waitFor();

        } catch (Exception e) {
            System.out.println("채점안됨");

            e.printStackTrace();
            // 예외 처리
        }
    }


    // 결과값 저정하기
    private synchronized void returnProgramResult(String resultPath, Long submitId) {
        try {
            System.out.println("들어왔음");
            String resultFileName = "result_" + submitId +".txt";

            // 채점 결과를 파일에서 읽어옴
            String result = new String(Files.readAllBytes(Path.of(resultPath, resultFileName)));

            System.out.println("결과" + result);

            // DB에서 해당 submit 엔터티 가져오기
            Optional<Submit> optionalSubmit = submitRepository.findById(submitId);

            if (optionalSubmit.isPresent()) {
                // 엔터티를 가져옴
                Submit submit = optionalSubmit.get();

                // 엔터티에 결과 저장
                submit.setResult(Long.valueOf(result));

                // 엔터티를 저장
                submitRepository.save(submit);
            } else {
                // 엔터티를 찾을 수 없는 경우 처리
                System.out.println("해당 submit을 찾을 수 없습니다. submitId: " + submitId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
