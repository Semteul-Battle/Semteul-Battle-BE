package Winter_Project.Semteul_Battle.domain.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserInquiryDto {
    private Long id;
    private String loginId;
    private String name;
    private String role;
}
