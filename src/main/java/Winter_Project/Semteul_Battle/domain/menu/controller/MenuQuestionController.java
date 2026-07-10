package Winter_Project.Semteul_Battle.domain.menu.controller;

import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.QuestionDeleteDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.QuestionDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.QuestionPageDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.QuestionUpdateDto;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.domain.menu.service.QuestionService;
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

    // ?饔낅떽?????????彛????熬곣뫖利???
@PostMapping("/createQuestion")
    public ResponseEntity<String> createQuestion(@RequestBody QuestionDto questionDto,
                                                 @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if(optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            // ?????諛몃마????????????熬곣뫖利???????????????????嚥싲갭큔???
Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            questionDto.setTime(currentTime);

            questionService.createQuestion(questionDto, users);
            return ResponseEntity.status(HttpStatus.CREATED).body("?饔낅떽?????????彛?????熬곣뫖利????????????");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("?????? ?饔낅떽???????????????깅즽????????놁졄.");
        }
    }

    // ?饔낅떽?????????彛??饔낅떽????ш낄?뉔뇡?꾩땡沃섏쥓??????怨쀫뮡????
@GetMapping("/inquiryQuestion")
    public ResponseEntity<QuestionPageDto> getQuestion(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        QuestionPageDto questionPageDto = questionService.getQuestionPage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(questionPageDto);
    }

    // ?饔낅떽?????????彛?????癰궽블뀯??
@PatchMapping("/updateQuestion")
    public ResponseEntity<String> updateQuestion(@RequestBody QuestionUpdateDto questionUpdateDto,
                                                 @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            questionUpdateDto.setTime(currentTime);
            questionService.updateQuestion(questionUpdateDto, loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("?饔낅떽?????????彛?????留???????癰궽블뀯???????????");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("?????? ?饔낅떽???????????????깅즽????????놁졄.");
        }
    }
    // ?饔낅떽?????????彛?????
@DeleteMapping("/deleteQuestion")
    public ResponseEntity<String> deleteQuestion(@RequestBody QuestionDeleteDto questionDeleteDto,
                                                 @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            questionService.deleteQuestion(questionDeleteDto,loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("?饔낅떽?????????彛?????留????????????????");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("?????? ?饔낅떽???????????????깅즽????????놁졄.");
        }
    }
}
