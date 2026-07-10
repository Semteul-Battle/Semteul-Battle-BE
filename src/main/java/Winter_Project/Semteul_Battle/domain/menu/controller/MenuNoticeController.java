package Winter_Project.Semteul_Battle.domain.menu.controller;

import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.menu.dto.notice.NoticeDeleteDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.notice.NoticeDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.notice.NoticePageDto;
import Winter_Project.Semteul_Battle.domain.menu.dto.notice.NoticeUpdateDto;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.domain.menu.service.NoticeService;
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

    // ?????怨룹?????????熬곣뫖利???
@PostMapping("/createNotice")
    public ResponseEntity<String> createNotice(@RequestBody NoticeDto noticeDto,
                                               @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if(optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            // ?????諛몃마????????????熬곣뫖利???????????????????嚥싲갭큔???
Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            noticeDto.setTime(currentTime);

            noticeService.createNotice(noticeDto, users);
            return ResponseEntity.status(HttpStatus.CREATED).body("?????怨룹??????????熬곣뫖利????????????");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("?????? ?饔낅떽???????????????깅즽????????놁졄.");
        }
    }


    // ?????怨룹?????????怨쀫뮡????
@GetMapping("/inquiryNotice")
    public ResponseEntity<NoticePageDto> getNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        NoticePageDto noticePageDto = noticeService.getNoticePage(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(noticePageDto);
    }

    // ?????怨룹??????????癰궽블뀯??
@PatchMapping("/updateNotice")
    public ResponseEntity<String> updateNotice(@RequestBody NoticeUpdateDto noticeUpdateDto,
                                               @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            noticeUpdateDto.setTime(currentTime);
            noticeService.updateNotice(noticeUpdateDto, loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("?????怨룹???????????癰궽블뀯???????????");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("?????? ?饔낅떽???????????????깅즽????????놁졄.");
        }
    }

    // ?????怨룹??????????
@DeleteMapping("/deleteNotice")
    public ResponseEntity<String> deleteNotice(@RequestBody NoticeDeleteDto noticeDeleteDto,
                                               @RequestHeader("Authorization") String token) {
        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);
        Optional<Users> optionalUsers = userRepository.findByLoginId(loginId);

        if (optionalUsers.isPresent()) {
            noticeService.deleteNotice(noticeDeleteDto,loginId);
            return ResponseEntity.status(HttpStatus.CREATED).body("?????怨룹????????????????????");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("?????? ?饔낅떽???????????????깅즽????????놁졄.");
        }
    }
}
