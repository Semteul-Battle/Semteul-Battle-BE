package Winter_Project.Semteul_Battle.controller.Users;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.dto.JwtToken;
import Winter_Project.Semteul_Battle.dto.Users.SignInDto;
import Winter_Project.Semteul_Battle.dto.Users.SignOutDto;
import Winter_Project.Semteul_Battle.service.Users.CustomUserDetailsService;
import Winter_Project.Semteul_Battle.service.Users.UserService;
import Winter_Project.Semteul_Battle.util.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class SignInOutController {
    private final CustomUserDetailsService customUserDetailsService;
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 로그인
    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDto signInDto, HttpServletResponse response) {
        String loginId = signInDto.getLoginId();
        String password = signInDto.getPassword();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

        // 제공된 password가 저장된(해시된) password와 일치하는지 확인합니다.
        if (encoder.matches(password, userDetails.getPassword())) {
            // Passwords match, JWT 토큰을 생성하고 반환합니다.
            JwtToken jwtToken = userService.signIn(loginId, password);
            log.info("사용자 '{}'의 로그인 성공", loginId);
            log.info("JWT 토큰 accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
            // 클라이언트 브라우저에 쿠키로 accessToken 저장
            Cookie cookie = new Cookie("accessToken", jwtToken.getAccessToken());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            // 이렇게 설정하면 HTTPS 프로토콜에서만 쿠키 전송
            cookie.setSecure(true);
            // 쿠키 만료 시간 설정 (예: 3시간)
            cookie.setMaxAge(3600 * 3);
            response.addCookie(cookie);
            // refreshToken저장
            redisUtil.setDataExpire(loginId, jwtToken.getRefreshToken(), 86400000);

            return jwtToken;
        } else {
            // Passwords do not match, 로그인 실패 처리
            log.warn("사용자 '{}'의 로그인 실패", loginId);
            throw new BadCredentialsException("유효하지 않은 사용자명 또는 비밀번호");
        }
    }

    // 로그아웃
    // loginId 주입 필요
    @PostMapping("/sign-out")
    public boolean signOut(@RequestBody SignOutDto signOutDto, @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer")) {
            String accessToken = token.substring(7); // "Bearer " 다음의 부분이 accessToken

            // redisDB에 저장된 리프레시 토큰을 삭제
            redisUtil.deleteData(signOutDto.getLoginId());

            // AccessToken을 블랙리스트에 추가하여 만료시키기
            Long expiration = jwtTokenProvider.getExpiration(accessToken);
            redisUtil.setBlackList(accessToken, "access_token", expiration);

            // 로그아웃 후에는 SecurityContext를 클리어해줘야 합니다.
            SecurityContextHolder.clearContext();
            return true;
        }
        return false;
    }
}
