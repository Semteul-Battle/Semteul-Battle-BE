package Winter_Project.Semteul_Battle.global.security.jwt;


import Winter_Project.Semteul_Battle.global.security.dto.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private final Key key;

    // application.yml?????secret ??????ル늉???????key??????
public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // User ??轅붽틓????????????ル늉???饔낅떽??????AccessToken, RefreshToken?????熬곣뫖利?????汝뷴젆??녷뉩??읂??饔낅떽?????????쇰뭽??
public JwtToken generateToken(Authentication authentication) {
        // ???????????ル늉????轅붽틓????筌뤾쑴裕?棺堉?뙴???
String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token ???熬곣뫖利???
Date accessTokenExpiresIn = new Date(now + 3600 * 3L * 1000);  // now??1000????鶯ㅺ동????숉룘??????썹땟??????녾컯????롪뎃???獄쏅챶痢?????쇰뮛????
String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token ???熬곣뫖利???
String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Jwt ????影?력???????쇰뮛?筌믡꺂??????????????影?력????????????얠????????????轅붽틓????????????????ロ뒌????饔낅떽?????????쇰뭽??
public Authentication getAuthentication(String accessToken) {
        // Jwt ????影?력??????쇰뮛?筌믡꺂????
Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("?????????轅붽틓???????먯땡沃섃넄?곈툣?????쎛 ????嶺뚮ㅎ???????影?력???????戮?Ĳ??");
        }

        // ?????????諛몃마????????????轅붽틓???????????ル늉????轅붽틓????筌뤾쑴裕?棺堉?뙴???
Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails ????ル늉????????濾??饔낅떽????????????닿튃癲?Authentication return
        // UserDetails: interface, User: UserDetails?????????노늾??class
UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // ????影?력????轅붽틓??????????棺堉?뤃???饔낅떽??影?곗몡???紐?亦껋꼦維????饔낅떽?????????쇰뭽??
public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    // accessToken???饔낅떽?????傭????????????ル늉????轅붽틓????筌뤾쑴裕???饔낅떽?????????쇰뭽??
public Long getExpiration(String accessToken) {
        return parseClaims(accessToken).getExpiration().getTime();
    }


    // accessToken
private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // ????影?력??????loginId ?????댄뱼???
public String extractLoginIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        String jwtToken = token.substring(7);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        String loginId = claims.getSubject();

        return loginId;
    }

}