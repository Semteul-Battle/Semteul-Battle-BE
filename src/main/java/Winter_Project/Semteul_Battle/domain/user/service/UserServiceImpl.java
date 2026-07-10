package Winter_Project.Semteul_Battle.domain.user.service;

import Winter_Project.Semteul_Battle.global.security.dto.JwtToken;
import Winter_Project.Semteul_Battle.domain.user.dto.SignUpDto;
import Winter_Project.Semteul_Battle.domain.user.dto.UserDto;

public interface UserServiceImpl {
    JwtToken signIn(String loginId, String password);

    UserDto signUp(SignUpDto signUpDto);
}
