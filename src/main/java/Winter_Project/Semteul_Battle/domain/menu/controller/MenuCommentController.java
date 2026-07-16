package Winter_Project.Semteul_Battle.domain.menu.controller;

import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentDeleteDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.CommentUpdateDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.CommentCheckDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.MenuCommentResponseDto;
import Winter_Project.Semteul_Battle.domain.menu.entity.MenuQuestion;
import Winter_Project.Semteul_Battle.domain.menu.exception.MenuException;
import Winter_Project.Semteul_Battle.domain.menu.repository.MenuQuestionRepository;
import Winter_Project.Semteul_Battle.domain.menu.service.CommentService;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/menu")
public class MenuCommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;
    private final MenuQuestionRepository menuQuestionRepository;

    @PostMapping("/createComment")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> createComment(
            @RequestBody @Valid CommentDto commentDto,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        Users users = getLoginUser(loginId);
        MenuQuestion menuQuestion = menuQuestionRepository.findById(commentDto.getQuestionId())
                .orElseThrow(() -> new MenuException(ErrorStatus._NOT_FOUND, "질문을 찾을 수 없습니다."));
        commentDto.recordWrittenAt(new Timestamp(System.currentTimeMillis()));
        commentService.createComment(commentDto, users, menuQuestion);
        return BaseResponse.onSuccess(SuccessStatus.CREATED, null);
    }

    @GetMapping("/inquiryComment")
    public List<MenuCommentResponseDto> getCommentsByQuestionId(@RequestBody @Valid CommentCheckDto commentCheckDto) {
        return commentService.getCommentsFromQuestion(commentCheckDto.getQuestionId()).stream()
                .map(MenuCommentResponseDto::from)
                .toList();
    }

    @PatchMapping("/updateComment")
    public BaseResponse<Void> updateComment(
            @RequestBody @Valid CommentUpdateDto commentUpdateDto,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        ensureLoginUser(loginId);
        commentUpdateDto.recordUpdatedAt(new Timestamp(System.currentTimeMillis()));
        commentService.updateComment(commentUpdateDto, loginId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @DeleteMapping("/deleteComment")
    public BaseResponse<Void> deleteComment(
            @RequestBody @Valid CommentDeleteDto commentDeleteDto,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        ensureLoginUser(loginId);
        commentService.deleteComment(commentDeleteDto, loginId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    private Users getLoginUser(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MenuException(ErrorStatus._NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    private void ensureLoginUser(String loginId) {
        if (!userRepository.existsByLoginId(loginId)) {
            throw new MenuException(ErrorStatus._NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
    }
}
