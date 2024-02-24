package Winter_Project.Semteul_Battle.dto.Contest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitPageDto<T> {
    private List<T> content; // 현재 페이지의 항목 리스트
    private int pageNumber; // 현재 페이지 번호
    private int pageSize; // 페이지당 항목 수
    private long totalElements; // 총 항목 수
    private int totalPages; // 총 페이지 수
    // 이전 페이지 여부
    private boolean hasPreviousPage;
    // 다음 페이지 여부
    private boolean hasNextPage;
}
