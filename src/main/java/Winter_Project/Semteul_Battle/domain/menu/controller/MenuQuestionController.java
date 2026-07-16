package Winter_Project.Semteul_Battle.domain.menu.controller;

import Winter_Project.Semteul_Battle.domain.menu.dto.request.QuestionDeleteDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.QuestionDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.QuestionUpdateDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.QuestionPageDto;
import Winter_Project.Semteul_Battle.domain.menu.exception.MenuException;
import Winter_Project.Semteul_Battle.domain.menu.service.QuestionService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/menu")
public class MenuQuestionController {

    private final QuestionService questionService;
    private final UserRepository userRepository;

    @PostMapping("/createQuestion")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> createQuestion(
            @RequestBody @Valid QuestionDto questionDto,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        Users users = getLoginUser(loginId);
        questionDto.setTime(new Timestamp(System.currentTimeMillis()));
        questionService.createQuestion(questionDto, users);
        return BaseResponse.onSuccess(SuccessStatus.CREATED, null);
    }

    @GetMapping("/inquiryQuestion")
    public QuestionPageDto getQuestion(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return questionService.getQuestionPage(page, size);
    }

    @PatchMapping("/updateQuestion")
    public BaseResponse<Void> updateQuestion(
            @RequestBody @Valid QuestionUpdateDto questionUpdateDto,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        ensureLoginUser(loginId);
        questionUpdateDto.setTime(new Timestamp(System.currentTimeMillis()));
        questionService.updateQuestion(questionUpdateDto, loginId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @DeleteMapping("/deleteQuestion")
    public BaseResponse<Void> deleteQuestion(
            @RequestBody @Valid QuestionDeleteDto questionDeleteDto,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        ensureLoginUser(loginId);
        questionService.deleteQuestion(questionDeleteDto, loginId);
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
