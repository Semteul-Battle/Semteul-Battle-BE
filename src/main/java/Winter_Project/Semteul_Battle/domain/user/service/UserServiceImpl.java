package Winter_Project.Semteul_Battle.domain.user.service;

import Winter_Project.Semteul_Battle.domain.user.dto.request.SignUpDto;
import Winter_Project.Semteul_Battle.domain.user.dto.response.UserDto;
import Winter_Project.Semteul_Battle.domain.user.dto.response.UserInquiryDto;
import Winter_Project.Semteul_Battle.domain.user.entity.UserRole;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.user.exception.UserException;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import Winter_Project.Semteul_Battle.global.security.dto.JwtToken;
import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

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
        validateSignUpRequest(signUpDto);

        String encodedPassword = bCryptPasswordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add(UserRole.USER.name());

        Users user = signUpDto.toEntity(encodedPassword);
        user.getRoles().addAll(roles);
        return UserDto.toDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public JwtToken signIn(String loginId, String password) {
        if (!StringUtils.hasText(loginId) || !StringUtils.hasText(password)) {
            throw new UserException(ErrorStatus._BAD_REQUEST, "아이디와 비밀번호를 입력해주세요.");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, password);

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            return jwtTokenProvider.generateToken(authentication);
        } catch (AuthenticationException e) {
            log.warn("login failed: {}", loginId);
            throw new UserException(ErrorStatus._UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    }

    @Override
    public Users getUserByUsername(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UserException(ErrorStatus._NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    @Override
    public UserInquiryDto getUserInformation(String loginId) {
        Users user = getUserByUsername(loginId);

        UserInquiryDto userInquiryDto = new UserInquiryDto();
        userInquiryDto.setLoginId(user.getLoginId());
        userInquiryDto.setName(user.getName());
        userInquiryDto.setRole(user.getRoles().toString());
        return userInquiryDto;
    }

    @Override
    public JwtToken tokenRenewal(String loginId) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return jwtTokenProvider.generateToken(authentication);
    }

    private void validateSignUpRequest(SignUpDto signUpDto) {
        if (signUpDto == null
                || !StringUtils.hasText(signUpDto.getLoginId())
                || !StringUtils.hasText(signUpDto.getPassword())
                || !StringUtils.hasText(signUpDto.getName())
                || !StringUtils.hasText(signUpDto.getEmail())
                || !StringUtils.hasText(signUpDto.getMajor())
                || !StringUtils.hasText(signUpDto.getUniversity())) {
            throw new UserException(ErrorStatus._BAD_REQUEST, "회원가입 필수 정보를 입력해주세요.");
        }

        if (userRepository.existsByLoginId(signUpDto.getLoginId())) {
            throw new UserException(ErrorStatus._CONFLICT, "이미 사용 중인 아이디입니다.");
        }

        userRepository.findByEmail(signUpDto.getEmail()).ifPresent(user -> {
            throw new UserException(ErrorStatus._CONFLICT, "이미 사용 중인 이메일입니다.");
        });
    }
}
