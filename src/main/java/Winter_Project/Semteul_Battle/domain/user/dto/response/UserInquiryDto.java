package Winter_Project.Semteul_Battle.domain.user.dto.response;

import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class UserInquiryDto {
    private Long id;
    private String loginId;
    private String name;
    private String role;

    public static UserInquiryDto from(Users user) {
        UserInquiryDto dto = new UserInquiryDto();
        dto.loginId = user.getLoginId();
        dto.name = user.getName();
        dto.role = user.getRoles().toString();
        return dto;
    }
}
