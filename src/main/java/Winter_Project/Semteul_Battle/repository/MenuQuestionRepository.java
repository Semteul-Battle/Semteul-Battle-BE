package Winter_Project.Semteul_Battle.repository;

import Winter_Project.Semteul_Battle.domain.MenuQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuQuestionRepository extends JpaRepository<MenuQuestion,Long> {
    Optional<MenuQuestion> findById(Long QuestionId);
}
