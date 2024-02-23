package Winter_Project.Semteul_Battle.service.Menu;

import Winter_Project.Semteul_Battle.domain.MenuNotice;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.MenuNotice.*;
import Winter_Project.Semteul_Battle.repository.MenuNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final MenuNoticeRepository menuNoticeRepository;

    // 공지사항 글쓰기 메서드
    public MenuNotice createNotice(NoticeDto noticeDto, Users users) {
        MenuNotice notice = MenuNotice.builder()
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .time(noticeDto.getTime())
                .users(users)
                .build();

        return menuNoticeRepository.save(notice);
    }

    // 공지사항 목록 조회 메서드
    public NoticePageDto getNoticePage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuNotice> noticePage = menuNoticeRepository.findAll(pageable);

        List<NoticeCheckDto> noticeDtoList = noticePage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        int totalPages = noticePage.getTotalPages();
        long totalElements = noticePage.getTotalElements();
        int currentPageNumber = noticePage.getNumber();

        boolean hasPreviousPage = currentPageNumber > 0; // 이전 페이지 여부
        boolean hasNextPage = currentPageNumber < totalPages - 1; // 다음 페이지 여부
        Integer prevPageNumber = hasPreviousPage ? currentPageNumber - 1 : null; // 이전 페이지 번호
        Integer nextPageNumber = hasNextPage ? currentPageNumber + 1 : null; // 다음 페이지 번호

        return new NoticePageDto(
                noticeDtoList,
                totalPages,
                totalElements,
                currentPageNumber,
                prevPageNumber,
                nextPageNumber
        );
    }

    // MenuNotice 엔티티를 NoticeCheckDto로 변환하는 메서드
    private NoticeCheckDto convertToDto(MenuNotice menuNotice) {
        String loginId = menuNotice.getUsers().getLoginId();
        return new NoticeCheckDto(
                loginId,
                menuNotice.getTitle(),
                menuNotice.getContent(),
                menuNotice.getTime()
        );
    }

    // 공지사항 수정 메서드
    public MenuNotice updateNotice(NoticeUpdateDto noticeUpdateDto, String loginId) {
        Long noticeId = noticeUpdateDto.getNoticeId();

        // 공지사항을 데이터베이스에서 가져옵니다.
        MenuNotice notice = menuNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 공지사항을 찾을 수 없습니다.", 1));

        // 해당 공지사항을 수정할 수 있는 권한이 있는지 확인합니다.
        if (!notice.getUsers().getLoginId().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 공지사항을 수정할 수 있는 권한이 없습니다.");
        }

        // 공지사항의 정보를 업데이트합니다.
        notice.setTitle(noticeUpdateDto.getTitle());
        notice.setContent(noticeUpdateDto.getContent());
        notice.setTime(noticeUpdateDto.getTime());

        // 업데이트된 공지사항을 저장하고 반환합니다.
        return menuNoticeRepository.save(notice);
    }

    // 공지사항 삭제 메서드
    public void deleteNotice(NoticeDeleteDto noticeDeleteDto, String loginId) {
        Long noticeId = noticeDeleteDto.getNoticeId();

        // 공지사항을 데이터베이스에서 가져옵니다.
        MenuNotice notice = menuNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 공지사항을 찾을 수 없습니다.", 1));

        // 해당 공지사항을 수정할 수 있는 권한이 있는지 확인합니다.
        if (!notice.getUsers().getLoginId().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 공지사항을 수정할 수 있는 권한이 없습니다.");
        }

        menuNoticeRepository.delete(notice);
    }
}
