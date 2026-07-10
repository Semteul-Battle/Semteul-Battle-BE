package Winter_Project.Semteul_Battle.domain.menu.repository;

import Winter_Project.Semteul_Battle.domain.menu.entity.MenuComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCommentRepository extends JpaRepository<MenuComment, Long> {
}