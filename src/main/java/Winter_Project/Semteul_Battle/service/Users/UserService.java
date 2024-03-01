package Winter_Project.Semteul_Battle.service.Users;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.dto.JwtToken;
import Winter_Project.Semteul_Battle.dto.Users.SignUpDto;
import Winter_Project.Semteul_Battle.dto.Users.UserDto;
import Winter_Project.Semteul_Battle.dto.Users.UserInquiryDto;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import Winter_Project.Semteul_Battle.util.RedisUtil;
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
public class UserService implements UserServiceImpl {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    @Override
    public UserDto signUp(SignUpDto signUpDto) {
        // Password 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");  // USER 권한 부여
        return UserDto.toDto(userRepository.save(signUpDto.toEntity(encodedPassword, roles)));
    }

    @Transactional
    @Override
    public JwtToken signIn(String loginId, String password) {
        // 1. loginId + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 User 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }

    public Users getUserByUsername(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + loginId));
    }

    public UserInquiryDto getUserInformation(String token) {

        String loginId = jwtTokenProvider.extractLoginIdFromToken(token);

        Optional<Users> user = userRepository.findByLoginId(loginId);

        UserInquiryDto userInquiryDto = new UserInquiryDto();
        userInquiryDto.setLoginId(user.get().getLoginId());
        userInquiryDto.setName(user.get().getName());
        userInquiryDto.setRole(user.get().getRoles().toString()); // 여러 역할 중 첫 번째 역할을 가져옴

        return userInquiryDto;
    }

    public JwtToken tokenRenewal(String loginId) {

        // 유효한 refresh token이라면 새로운 access token 생성
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        JwtToken newAccessToken = jwtTokenProvider.generateToken(authentication);

        return newAccessToken;
    }

}