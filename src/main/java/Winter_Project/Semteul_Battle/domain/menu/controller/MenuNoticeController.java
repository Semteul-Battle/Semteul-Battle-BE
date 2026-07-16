package Winter_Project.Semteul_Battle.domain.menu.controller;

import Winter_Project.Semteul_Battle.domain.menu.dto.request.NoticeDeleteDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.NoticeDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.NoticeUpdateDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.NoticePageDto;
import Winter_Project.Semteul_Battle.domain.menu.exception.MenuException;
import Winter_Project.Semteul_Battle.domain.menu.service.NoticeService;
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
public class MenuNoticeController {

    private final NoticeService noticeService;
    private final UserRepository userRepository;

    @PostMapping("/createNotice")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> createNotice(
            @RequestBody @Valid NoticeDto noticeDto,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        Users users = getLoginUser(loginId);
        noticeDto.recordWrittenAt(new Timestamp(System.currentTimeMillis()));
        noticeService.createNotice(noticeDto, users);
        return BaseResponse.onSuccess(SuccessStatus.CREATED, null);
    }

    @GetMapping("/inquiryNotice")
    public NoticePageDto getNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return noticeService.getNoticePage(page, size);
    }

    @PatchMapping("/updateNotice")
    public BaseResponse<Void> updateNotice(
            @RequestBody @Valid NoticeUpdateDto noticeUpdateDto,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        ensureLoginUser(loginId);
        noticeUpdateDto.recordUpdatedAt(new Timestamp(System.currentTimeMillis()));
        noticeService.updateNotice(noticeUpdateDto, loginId);
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @DeleteMapping("/deleteNotice")
    public BaseResponse<Void> deleteNotice(
            @RequestBody @Valid NoticeDeleteDto noticeDeleteDto,
            @AuthenticationPrincipal(expression = "username") String loginId
    ) {
        ensureLoginUser(loginId);
        noticeService.deleteNotice(noticeDeleteDto, loginId);
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
