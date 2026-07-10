package Winter_Project.Semteul_Battle.domain.user.controller;

import Winter_Project.Semteul_Battle.domain.user.dto.request.SignInDto;
import Winter_Project.Semteul_Battle.domain.user.exception.UserException;
import Winter_Project.Semteul_Battle.domain.user.service.CustomUserDetailsService;
import Winter_Project.Semteul_Battle.domain.user.service.UserService;
import Winter_Project.Semteul_Battle.global.security.dto.JwtToken;
import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.global.util.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class SignInOutController {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDto signInDto, HttpServletResponse response) {
        String loginId = signInDto.getLoginId();
        String password = signInDto.getPassword();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
        if (!encoder.matches(password, userDetails.getPassword())) {
            log.warn("login failed: {}", loginId);
            throw new UserException(ErrorStatus._UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        JwtToken jwtToken = userService.signIn(loginId, password);
        log.info("login requested: {}", loginId);
        redisUtil.setDataExpire(loginId, jwtToken.getRefreshToken(), 86400000);
        return jwtToken;
    }

    @PostMapping("/sign-out")
    public boolean signOut(
            @AuthenticationPrincipal(expression = "username") String loginId,
            @RequestHeader("Authorization") String token
    ) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UserException(ErrorStatus._UNAUTHORIZED, "유효하지 않은 인증 토큰입니다.");
        }

        String accessToken = token.substring(7);
        redisUtil.deleteData(loginId);

        Long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisUtil.setBlackList(accessToken, "access_token", expiration);

        SecurityContextHolder.clearContext();
        return true;
    }

    @PostMapping("/renewalToken")
    public JwtToken renewalToken(@RequestParam String loginId) {
        String refreshTokenFromId = redisUtil.getData(loginId);
        if (refreshTokenFromId == null) {
            throw new UserException(ErrorStatus._BAD_REQUEST, "저장된 refresh token이 없습니다.");
        }
        if (!jwtTokenProvider.validateToken(refreshTokenFromId)) {
            throw new UserException(ErrorStatus._UNAUTHORIZED, "refresh token이 유효하지 않습니다.");
        }
        return userService.tokenRenewal(loginId);
    }
}
