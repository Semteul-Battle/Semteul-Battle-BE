package Winter_Project.Semteul_Battle.dto.Users;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UserInquiryDto {
    private String loginId;
    private String name;
    private String role;
}
