package Winter_Project.Semteul_Battle.domain.user.service;

import Winter_Project.Semteul_Battle.domain.user.dto.request.SignUpDto;
import Winter_Project.Semteul_Battle.domain.user.dto.response.UserDto;
import Winter_Project.Semteul_Battle.domain.user.dto.response.UserInquiryDto;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.global.security.dto.JwtToken;

public interface UserService {

    UserDto signUp(SignUpDto signUpDto);

    JwtToken signIn(String loginId, String password);

    Users getUserByUsername(String loginId);

    UserInquiryDto getUserInformation(String loginId);

    JwtToken tokenRenewal(String loginId);
}
