package Winter_Project.Semteul_Battle.controller.Menu;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.MenuNotice.NoticeDeleteDto;
import Winter_Project.Semteul_Battle.dto.MenuNotice.NoticeDto;
import Winter_Project.Semteul_Battle.dto.MenuNotice.NoticePageDto;
import Winter_Project.Semteul_Battle.dto.MenuNotice.NoticeUpdateDto;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import Winter_Project.Semteul_Battle.service.Menu.NoticeService;
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
public class MenuNoticeController {
    private final NoticeService noticeService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 공지사항 생성
    @PostMapping("/createNotice")
    public ResponseEntity<String> createNotice(@RequestBody NoticeDto noticeDto,
                                               @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if(optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            // 현재 시간을 생성 시간으로 설정
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            noticeDto.setTime(currentTime);

            noticeService.createNotice(noticeDto, users);
            return ResponseEntity.status(HttpStatus.CREATED).body("공지사항이 생성되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }


    // 공지사항 조회
    @GetMapping("/inquiryNotice")
    public ResponseEntity<NoticePageDto> getNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        NoticePageDto noticePageDto = noticeService.getNoticePage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(noticePageDto);
    }

    // 공지사항 수정
    @PatchMapping("/updateNotice")
    public ResponseEntity<String> updateNotice(@RequestBody NoticeUpdateDto noticeUpdateDto,
                                               @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            noticeUpdateDto.setTime(currentTime);
            noticeService.updateNotice(noticeUpdateDto, loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("공지사항이 수정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }

    // 공지사항 삭제
    @DeleteMapping("/deleteNotice")
    public ResponseEntity<String> deleteNotice(@RequestBody NoticeDeleteDto noticeDeleteDto,
                                               @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            noticeService.deleteNotice(noticeDeleteDto,loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("공지사항이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
    }
}
