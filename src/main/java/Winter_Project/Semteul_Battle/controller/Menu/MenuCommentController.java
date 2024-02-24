package Winter_Project.Semteul_Battle.controller.Menu;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.MenuComment;
import Winter_Project.Semteul_Battle.domain.MenuQuestion;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.MenuComment.CommentCheckDto;
import Winter_Project.Semteul_Battle.dto.MenuComment.CommentDeleteDto;
import Winter_Project.Semteul_Battle.dto.MenuComment.CommentDto;
import Winter_Project.Semteul_Battle.dto.MenuComment.CommentUpdateDto;
import Winter_Project.Semteul_Battle.repository.MenuQuestionRepository;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import Winter_Project.Semteul_Battle.service.Menu.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    // 댓글 생성
    @PostMapping("/createComment")
    public ResponseEntity<String> createComment(@RequestBody CommentDto commentDto,
                                                @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);
        Optional<MenuQuestion> optionalMenuQuestion = menuQuestionRepository.findById(commentDto.getQuestionId());

        if(optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            MenuQuestion menuQuestion = optionalMenuQuestion.get();
            // 현재 시간을 생성 시간으로 설정
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            commentDto.setTime(currentTime);

            commentService.createComment(commentDto, users, menuQuestion);
            return ResponseEntity.status(HttpStatus.CREATED).body("댓글이 생성되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    // 질문 게시글의 댓글 조회
    @GetMapping("/inquiryComment")
    public ResponseEntity<List<MenuComment>> getCommentsByQuestionId(@RequestBody CommentCheckDto commentCheckDto) {
        try {
            List<MenuComment> comments = commentService.getCommentsFromQuestion(commentCheckDto.getQuestionId());
            return ResponseEntity.ok(comments);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 질문에 대한 댓글을 찾을 수 없습니다.", e);
        }
    }

    // 댓글 수정
    @PatchMapping("/updateComment")
    public ResponseEntity<String> updateComment(@RequestBody CommentUpdateDto commentUpdateDto,
                                                @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            commentUpdateDto.setTime(currentTime);
            commentService.updateComment(commentUpdateDto, loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("댓글이 수정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    // 댓글 삭제
    @DeleteMapping("/deleteComment")
    public ResponseEntity<String> deleteComment(@RequestBody CommentDeleteDto commentDeleteDto,
                                                @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            commentService.deleteComment(commentDeleteDto, loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("댓글이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
}