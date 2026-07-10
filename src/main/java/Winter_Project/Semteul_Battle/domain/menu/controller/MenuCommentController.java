package Winter_Project.Semteul_Battle.domain.menu.controller;



import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.domain.menu.exception.MenuException;
import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.menu.entity.MenuComment;
import Winter_Project.Semteul_Battle.domain.menu.entity.MenuQuestion;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.CommentCheckDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentDeleteDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentUpdateDto;
import Winter_Project.Semteul_Battle.domain.menu.repository.MenuQuestionRepository;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.domain.menu.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/menu")
public class MenuCommentController {
    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final MenuQuestionRepository menuQuestionRepository;

    // ??? ???熬곣뫖利???
@PostMapping("/createComment")
    public ResponseEntity<String> createComment(@RequestBody CommentDto commentDto,
                                                @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);
        Optional<MenuQuestion> optionalMenuQuestion = menuQuestionRepository.findById(commentDto.getQuestionId());

        if(optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            MenuQuestion menuQuestion = optionalMenuQuestion.get();
            // ?????諛몃마????????????熬곣뫖利???????????????????嚥싲갭큔???
Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            commentDto.setTime(currentTime);

            commentService.createComment(commentDto, users, menuQuestion);
            return ResponseEntity.status(HttpStatus.CREATED).body("????????熬곣뫖利????????????");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("?????? ?饔낅떽???????????????깅즽????????놁졄.");
        }
    }

    // ?饔낅떽?????????彛???棺堉?뤃??????룸챷??????? ???怨쀫뮡????
@GetMapping("/inquiryComment")
    public ResponseEntity<List<MenuComment>> getCommentsByQuestionId(@RequestBody CommentCheckDto commentCheckDto) {
        try {
            List<MenuComment> comments = commentService.getCommentsFromQuestion(commentCheckDto.getQuestionId());
            return ResponseEntity.ok(comments);
        } catch (EmptyResultDataAccessException e) {
            throw new MenuException(ErrorStatus._NOT_FOUND, "??????饔낅떽?????????彛????????????饔낅떽???????????????깅즽????????놁졄.");
        }
    }

    // ??? ????癰궽블뀯??
@PatchMapping("/updateComment")
    public ResponseEntity<String> updateComment(@RequestBody CommentUpdateDto commentUpdateDto,
                                                @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            commentUpdateDto.setTime(currentTime);
            commentService.updateComment(commentUpdateDto, loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("?????????癰궽블뀯???????????");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("?????? ?饔낅떽???????????????깅즽????????놁졄.");
        }
    }

    // ??? ????
@DeleteMapping("/deleteComment")
    public ResponseEntity<String> deleteComment(@RequestBody CommentDeleteDto commentDeleteDto,
                                                @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            commentService.deleteComment(commentDeleteDto, loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("??????????????????");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("?????? ?饔낅떽???????????????깅즽????????놁졄.");
        }
    }
}
