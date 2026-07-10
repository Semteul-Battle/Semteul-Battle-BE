package Winter_Project.Semteul_Battle.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
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
