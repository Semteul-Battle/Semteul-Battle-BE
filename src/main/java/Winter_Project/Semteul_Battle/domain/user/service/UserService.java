package Winter_Project.Semteul_Battle.domain.user.service;

import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.global.security.dto.JwtToken;
import Winter_Project.Semteul_Battle.domain.user.dto.request.SignUpDto;
import Winter_Project.Semteul_Battle.domain.user.dto.response.UserDto;
import Winter_Project.Semteul_Battle.domain.user.dto.response.UserInquiryDto;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.global.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public UserDto signUp(SignUpDto signUpDto);
    public JwtToken signIn(String loginId, String password);
    public Users getUserByUsername(String loginId);
    public UserInquiryDto getUserInformation(String token);
    public JwtToken tokenRenewal(String loginId);
}
