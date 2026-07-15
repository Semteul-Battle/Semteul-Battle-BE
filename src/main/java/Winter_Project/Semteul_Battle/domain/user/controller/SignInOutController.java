package Winter_Project.Semteul_Battle.domain.user.controller;

import Winter_Project.Semteul_Battle.domain.user.dto.request.SignInDto;
import Winter_Project.Semteul_Battle.domain.user.exception.UserException;
import Winter_Project.Semteul_Battle.domain.user.service.UserService;
import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.security.dto.JwtToken;
import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import Winter_Project.Semteul_Battle.global.status.SuccessStatus;
import Winter_Project.Semteul_Battle.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class SignInOutController {

    private static final String AUTH_REFRESH_KEY_PREFIX = "auth:refresh:";
    private static final long REFRESH_TOKEN_TTL_SECONDS = 86_400L;

    private final UserService userService;
    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-in")
    public BaseResponse<JwtToken> signIn(@RequestBody SignInDto signInDto) {
        JwtToken jwtToken = userService.signIn(signInDto.getLoginId(), signInDto.getPassword());
        redisUtil.setDataExpire(refreshTokenKey(signInDto.getLoginId()), jwtToken.getRefreshToken(), REFRESH_TOKEN_TTL_SECONDS);
        log.info("login requested: {}", signInDto.getLoginId());
        return BaseResponse.onSuccess(SuccessStatus.OK, jwtToken);
    }

    @PostMapping("/sign-out")
    public BaseResponse<Void> signOut(
            @AuthenticationPrincipal(expression = "username") String loginId,
            @RequestHeader("Authorization") String token
    ) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UserException(ErrorStatus._UNAUTHORIZED, "유효하지 않은 인증 토큰입니다.");
        }

        String accessToken = token.substring(7);
        redisUtil.deleteData(refreshTokenKey(loginId));

        Long expiration = jwtTokenProvider.getExpiration(accessToken);
        redisUtil.setBlackList(accessToken, "access_token", expiration);

        SecurityContextHolder.clearContext();
        return BaseResponse.onSuccess(SuccessStatus.OK, null);
    }

    @PostMapping("/renewalToken")
    public BaseResponse<JwtToken> renewalToken(@RequestHeader("Refresh-Token") String refreshToken) {
        if (!StringUtils.hasText(refreshToken) || !refreshToken.startsWith("Bearer ")) {
            throw new UserException(ErrorStatus._UNAUTHORIZED, "유효하지 않은 refresh token입니다.");
        }

        String token = refreshToken.substring(7);
        if (!jwtTokenProvider.validateToken(token) || !jwtTokenProvider.isRefreshToken(token)) {
            throw new UserException(ErrorStatus._UNAUTHORIZED, "refresh token이 유효하지 않습니다.");
        }

        String loginId = jwtTokenProvider.getLoginId(token);
        JwtToken jwtToken = userService.tokenRenewal(loginId);
        boolean renewed = redisUtil.compareAndSetDataExpire(
                refreshTokenKey(loginId),
                token,
                jwtToken.getRefreshToken(),
                REFRESH_TOKEN_TTL_SECONDS
        );

        if (!renewed) {
            throw new UserException(ErrorStatus._UNAUTHORIZED, "refresh token이 만료되었거나 이미 사용되었습니다.");
        }

        return BaseResponse.onSuccess(SuccessStatus.OK, jwtToken);
    }

    private String refreshTokenKey(String loginId) {
        return AUTH_REFRESH_KEY_PREFIX + loginId;
    }
}
