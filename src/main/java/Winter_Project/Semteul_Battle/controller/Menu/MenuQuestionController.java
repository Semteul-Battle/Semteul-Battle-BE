package Winter_Project.Semteul_Battle.controller.Menu;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.MenuQuestion.QuestionDeleteDto;
import Winter_Project.Semteul_Battle.dto.MenuQuestion.QuestionDto;
import Winter_Project.Semteul_Battle.dto.MenuQuestion.QuestionPageDto;
import Winter_Project.Semteul_Battle.dto.MenuQuestion.QuestionUpdateDto;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import Winter_Project.Semteul_Battle.service.Menu.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/menu")
public class MenuQuestionController {
    private final QuestionService questionService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 질문 생성
    @PostMapping("/createQuestion")
    public ResponseEntity<String> createQuestion(@RequestBody QuestionDto questionDto,
                                                 @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if(optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            // 현재 시간을 생성 시간으로 설정
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            questionDto.setTime(currentTime);

            questionService.createQuestion(questionDto, users);
            return ResponseEntity.status(HttpStatus.CREATED).body("질문이 생성되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    // 질문 목록 조회
    @GetMapping("/inquiryQuestion")
    public ResponseEntity<QuestionPageDto> getQuestion(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        QuestionPageDto questionPageDto = questionService.getQuestionPage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(questionPageDto);
    }

    // 질문 수정
    @PatchMapping("/updateQuestion")
    public ResponseEntity<String> updateQuestion(@RequestBody QuestionUpdateDto questionUpdateDto,
                                                 @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            questionUpdateDto.setTime(currentTime);
            questionService.updateQuestion(questionUpdateDto, loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("질문글이 수정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
    // 질문 삭제
    @DeleteMapping("/deleteQuestion")
    public ResponseEntity<String> deleteQuestion(@RequestBody QuestionDeleteDto questionDeleteDto,
                                                 @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            questionService.deleteQuestion(questionDeleteDto,loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("질문글이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
}
