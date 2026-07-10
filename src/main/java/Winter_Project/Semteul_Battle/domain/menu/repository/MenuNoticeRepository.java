package Winter_Project.Semteul_Battle.domain.menu.repository;

import Winter_Project.Semteul_Battle.domain.menu.entity.MenuNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuNoticeRepository extends JpaRepository<MenuNotice, Long> {
    Optional<MenuNotice> findById(Long NoticeId);
}
