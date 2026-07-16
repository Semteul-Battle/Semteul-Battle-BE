package Winter_Project.Semteul_Battle.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPageDto {
    private List<QuestionCheckDto> questions;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private Integer prevPage;
    private Integer nextPage;
}
