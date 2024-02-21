package Winter_Project.Semteul_Battle.config;

import Winter_Project.Semteul_Battle.config.jwt.JwtAuthenticationFilter;
import Winter_Project.Semteul_Battle.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
                .httpBasic().disable()
                .csrf().disable()
                // JWT를 사용하기 때문에 세션을 사용하지 않음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                // 해당 API에 대해서는 모든 요청을 허가
                .requestMatchers("/users/**").permitAll()
                .requestMatchers("/contests/**").permitAll()
                .requestMatchers("/submit/**").permitAll()
                .requestMatchers("/contest/contestCreate").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/contest/contestDelete/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/**").hasAnyAuthority("ROLE_ADMIN")
                // USER 권한이 있어야 요청할 수 있음
//                .requestMatchers("/members/test").hasRole("USER")
                // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
                .anyRequest().authenticated()
                .and()
                // 로그인
                .formLogin()
//                .loginPage("/members/sign-in")
                .and()
                // 로그아웃
                .logout()
                .logoutUrl("/members/sign-out") // 로그아웃 URL 지정
                .logoutSuccessUrl("/members/sign-in") // 로그아웃 성공 시 이동할 URL 지정
                .invalidateHttpSession(true) // 세션 무효화
                .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                .and()
                // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class).build();
    }

    // password 암호화는 BCryptPasswordEncoder 사용
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}