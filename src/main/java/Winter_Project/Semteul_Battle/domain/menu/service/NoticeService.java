package Winter_Project.Semteul_Battle.domain.menu.service;

import Winter_Project.Semteul_Battle.domain.menu.entity.MenuNotice;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.menu.dto.request.*;
import Winter_Project.Semteul_Battle.domain.menu.dto.response.*;
import Winter_Project.Semteul_Battle.domain.menu.repository.MenuNoticeRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

public interface NoticeService {
    public MenuNotice createNotice(NoticeDto noticeDto, Users users);
    public NoticePageDto getNoticePage(int page, int size);
    public MenuNotice updateNotice(NoticeUpdateDto noticeUpdateDto, String loginId);
    public void deleteNotice(NoticeDeleteDto noticeDeleteDto, String loginId);
}
