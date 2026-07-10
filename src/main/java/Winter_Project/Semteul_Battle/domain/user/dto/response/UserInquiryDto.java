package Winter_Project.Semteul_Battle.domain.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UserInquiryDto {
    private Long id;
    private String loginId;
    private String name;
    private String role;
}
