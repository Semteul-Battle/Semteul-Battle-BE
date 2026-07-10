package Winter_Project.Semteul_Battle.domain.user.service;



import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.domain.user.exception.UserException;
import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.global.security.dto.JwtToken;
import Winter_Project.Semteul_Battle.domain.user.dto.request.SignUpDto;
import Winter_Project.Semteul_Battle.domain.user.dto.response.UserDto;
import Winter_Project.Semteul_Battle.domain.user.dto.response.UserInquiryDto;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    @Override
    public UserDto signUp(SignUpDto signUpDto) {

String encodedPassword = bCryptPasswordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");
return UserDto.toDto(userRepository.save(signUpDto.toEntity(encodedPassword, roles)));
    }

    @Transactional
    @Override
    public JwtToken signIn(String loginId, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);


Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);


JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    public Users getUserByUsername(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UserException(ErrorStatus._NOT_FOUND, "User not found with username: " + loginId));
    }

    public UserInquiryDto getUserInformation(String loginId) {
        Optional<Users> user = userRepository.findByLoginId(loginId);

        UserInquiryDto userInquiryDto = new UserInquiryDto();
        userInquiryDto.setLoginId(user.get().getLoginId());
        userInquiryDto.setName(user.get().getName());
        userInquiryDto.setRole(user.get().getRoles().toString());
return userInquiryDto;
    }

    public JwtToken tokenRenewal(String loginId) {


UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        JwtToken newAccessToken = jwtTokenProvider.generateToken(authentication);

        return newAccessToken;
    }

}
