package Winter_Project.Semteul_Battle.domain.menu.repository;

import Winter_Project.Semteul_Battle.domain.menu.entity.MenuComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuCommentRepository extends JpaRepository<MenuComment, Long> {
    Optional<MenuComment> findById(Long CommentId);
    List<MenuComment> findByMenuQuestion_Id(Long QuestionId);
}