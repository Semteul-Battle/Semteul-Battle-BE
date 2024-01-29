package Winter_Project.Semteul_Battle.service.Users;

import Winter_Project.Semteul_Battle.dto.JwtToken;
import Winter_Project.Semteul_Battle.dto.Users.SignUpDto;
import Winter_Project.Semteul_Battle.dto.Users.UserDto;

public interface UserServiceImpl {
    JwtToken signIn(String loginId, String password);

    UserDto signUp(SignUpDto signUpDto);
}
