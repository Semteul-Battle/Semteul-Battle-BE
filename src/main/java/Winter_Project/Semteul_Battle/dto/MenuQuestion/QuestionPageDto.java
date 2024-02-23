package Winter_Project.Semteul_Battle.dto.MenuQuestion;

import Winter_Project.Semteul_Battle.dto.MenuNotice.NoticeCheckDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPageDto {
    private List<QuestionCheckDto> questions; // 페이징된 공지사항 목록
    private int totalPages; // 전체 페이지 수
    private long totalElements; // 전체 공지사항 수
    private int currentPage; // 현재 페이지 번호
    private Integer prevPage; // 이전 페이지
    private Integer nextPage; // 다음 페이지
}
