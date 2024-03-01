package Winter_Project.Semteul_Battle.controller.Users;

import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.dto.JwtToken;
import Winter_Project.Semteul_Battle.dto.Users.SignInDto;
import Winter_Project.Semteul_Battle.dto.Users.SignOutDto;
import Winter_Project.Semteul_Battle.service.Users.CustomUserDetailsService;
import Winter_Project.Semteul_Battle.service.Users.UserService;
import Winter_Project.Semteul_Battle.util.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> signIn(@RequestBody SignInDto signInDto, HttpServletResponse response) {
        String loginId = signInDto.getLoginId();
        String password = signInDto.getPassword();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

        // 제공된 password가 저장된(해시된) password와 일치하는지 확인합니다.
        if (encoder.matches(password, userDetails.getPassword())) {
            // Passwords match, JWT 토큰을 생성하고 반환합니다.
            JwtToken jwtToken = userService.signIn(loginId, password);
            log.info("사용자 '{}'의 로그인 성공", loginId);
            log.info("JWT 토큰 accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
            redisUtil.setDataExpire(loginId, jwtToken.getRefreshToken(), 86400000);

            return ResponseEntity.ok("로그인 성공\n"+"AccessToken : "+jwtToken.getAccessToken()+"\n"+"RefreshToken : "+jwtToken.getRefreshToken());
        } else {
            // Passwords do not match, 로그인 실패 처리
            log.warn("사용자 '{}'의 로그인 실패", loginId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("아이디 혹은 패스워드가 올바르지 않습니다.");
        }
    }

    // 로그아웃
    // loginId 주입 필요
    @PostMapping("/sign-out")
    public boolean signOut(@RequestBody SignOutDto signOutDto, @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
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

    // accessToken 재발급 요청
    @PostMapping("/renewalToken")
    public ResponseEntity<String> renewalToken(@RequestParam String loginId) {

        // redis에 들어있는 refreshToken을 추출함
        String refreshTokenFromId = redisUtil.getData(loginId);

        // refreshToken의 유효성을 체크하고, 존재유무를 체크하여 검증
        if (refreshTokenFromId != null) {
            if (jwtTokenProvider.validateToken(refreshTokenFromId)) {
                JwtToken newToken = userService.tokenRenewal(loginId);
                return ResponseEntity.ok("토큰이 정상적으로 발급되었습니다."+"\n"+
                        "AccessToken : "+newToken.getAccessToken()+"\n"+"RefreshToken : "+newToken.getRefreshToken());
            } else {
                return ResponseEntity.badRequest().body("토큰이 만료되었습니다.");
            }
        } else {
            return ResponseEntity.badRequest().body("토큰이 존재하지 않습니다.");
        }
    }
}