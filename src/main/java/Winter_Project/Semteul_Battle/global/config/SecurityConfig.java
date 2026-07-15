package Winter_Project.Semteul_Battle.global.config;

import Winter_Project.Semteul_Battle.global.security.jwt.JwtAuthenticationEntryPoint;
import Winter_Project.Semteul_Battle.global.security.jwt.JwtAuthenticationFilter;
import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.global.response.BaseResponse;
import Winter_Project.Semteul_Battle.global.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(
                                "/users/sign-in",
                                "/users/sign-up",
                                "/users/id-check",
                                "/users/send-email",
                                "/users/verification",
                                "/users/send",
                                "/users/find",
                                "/users/update",
                                "/users/renewalToken"
                        ).permitAll()
                        .requestMatchers(
                                "/contests/all",
                                "/contests/ongoing",
                                "/contests/scheduled",
                                "/contests/finished",
                                "/menu/inquiryNotice",
                                "/menu/inquiryQuestion",
                                "/menu/inquiryComment"
                        ).permitAll()
                        .requestMatchers(
                                "/contest/id-designate",
                                "/contest/contestCreate",
                                "/contest/contestDelete/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                "/users/**",
                                "/contests/**",
                                "/contest/**",
                                "/submit/**",
                                "/menu/**",
                                "/upload"
                        ).authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json;charset=UTF-8");
                            objectMapper.writeValue(
                                    response.getOutputStream(),
                                    BaseResponse.onFailure(ErrorStatus._FORBIDDEN, accessDeniedException.getMessage())
                            );
                        })
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
