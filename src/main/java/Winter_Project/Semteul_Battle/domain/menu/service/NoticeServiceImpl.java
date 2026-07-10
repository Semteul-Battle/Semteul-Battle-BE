package Winter_Project.Semteul_Battle.domain.menu.service;



import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.domain.menu.exception.MenuException;
import Winter_Project.Semteul_Battle.domain.menu.entity.MenuNotice;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.*;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.*;
import Winter_Project.Semteul_Battle.domain.menu.repository.MenuNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final MenuNoticeRepository menuNoticeRepository;


public MenuNotice createNotice(NoticeDto noticeDto, Users users) {
        MenuNotice notice = MenuNotice.builder()
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .time(noticeDto.getTime())
                .users(users)
                .build();

        return menuNoticeRepository.save(notice);
    }


public NoticePageDto getNoticePage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MenuNotice> noticePage = menuNoticeRepository.findAll(pageable);

        List<NoticeCheckDto> noticeDtoList = noticePage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        int totalPages = noticePage.getTotalPages();
        long totalElements = noticePage.getTotalElements();
        int currentPageNumber = noticePage.getNumber();

        boolean hasPreviousPage = currentPageNumber > 0;
boolean hasNextPage = currentPageNumber < totalPages - 1;
        Integer prevPageNumber = hasPreviousPage ? currentPageNumber - 1 : null;
        Integer nextPageNumber = hasNextPage ? currentPageNumber + 1 : null;
return new NoticePageDto(
                noticeDtoList,
                totalPages,
                totalElements,
                currentPageNumber,
                prevPageNumber,
                nextPageNumber
        );
    }


private NoticeCheckDto convertToDto(MenuNotice menuNotice) {
        String loginId = menuNotice.getUsers().getLoginId();
        return new NoticeCheckDto(
                loginId,
                menuNotice.getTitle(),
                menuNotice.getContent(),
                menuNotice.getTime()
        );
    }


public MenuNotice updateNotice(NoticeUpdateDto noticeUpdateDto, String loginId) {
        Long noticeId = noticeUpdateDto.getNoticeId();


MenuNotice notice = menuNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new MenuException(ErrorStatus._NOT_FOUND, "공지사항을 찾을 수 없습니다."));


if (!notice.getUsers().getLoginId().equals(loginId)) {
            throw new MenuException(ErrorStatus._FORBIDDEN, "공지사항을 수정하거나 삭제할 권한이 없습니다.");
        }


        notice.setTitle(noticeUpdateDto.getTitle());
        notice.setContent(noticeUpdateDto.getContent());
        notice.setTime(noticeUpdateDto.getTime());


return menuNoticeRepository.save(notice);
    }


public void deleteNotice(NoticeDeleteDto noticeDeleteDto, String loginId) {
        Long noticeId = noticeDeleteDto.getNoticeId();


MenuNotice notice = menuNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new MenuException(ErrorStatus._NOT_FOUND, "공지사항을 찾을 수 없습니다."));


if (!notice.getUsers().getLoginId().equals(loginId)) {
            throw new MenuException(ErrorStatus._FORBIDDEN, "공지사항을 수정하거나 삭제할 권한이 없습니다.");
        }

        menuNoticeRepository.delete(notice);
    }
}
