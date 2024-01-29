package Winter_Project.Semteul_Battle.dto.Users;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class SignOutDto {
    private String loginId;
}
