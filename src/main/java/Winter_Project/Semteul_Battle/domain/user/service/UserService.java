package Winter_Project.Semteul_Battle.domain.user.service;

import Winter_Project.Semteul_Battle.global.security.jwt.JwtTokenProvider;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.global.security.dto.JwtToken;
import Winter_Project.Semteul_Battle.domain.user.dto.SignUpDto;
import Winter_Project.Semteul_Battle.domain.user.dto.UserDto;
import Winter_Project.Semteul_Battle.domain.user.dto.UserInquiryDto;
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
public class UserService implements UserServiceImpl {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    @Override
    public UserDto signUp(SignUpDto signUpDto) {
        // Password ????????
String encodedPassword = bCryptPasswordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");  // USER ????????????뼿???
return UserDto.toDto(userRepository.save(signUpDto.toEntity(encodedPassword, roles)));
    }

    @Transactional
    @Override
    public JwtToken signIn(String loginId, String password) {
        // 1. loginId + password ?????????力?肉?????????Authentication ????ル늉?????????熬곣뫖利???        // ?????authentication ?? ??轅붽틓?????믊븐꽫????????轅붽틓??????????汝뷴젆??녷뉩??읂?authenticated ????ル늉????false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

        // 2. ???嚥싲갭큔?源????棺堉?뤃???? authenticate() ?饔낅떽?????????쇰뭽??? ?????????嫄????User ????????棺堉?뤃?????饔낅떽?????嶺뚮ㅎ???        // authenticate ?饔낅떽?????????쇰뭽??? ????????????CustomUserDetailsService ??????饔낅떽???????loadUserByUsername ?饔낅떽?????????쇰뭽??????????
Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. ??轅붽틓?????믊븐꽫???轅붽틓???????????????力?肉?????????JWT ????影?력?????熬곣뫖利???
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
        userInquiryDto.setRole(user.get().getRoles().toString()); // ?????????????癲??????????????ル늉????轅붽틓????筌뤾벳?
return userInquiryDto;
    }

    public JwtToken tokenRenewal(String loginId) {

        // ?????????refresh token???????蹂κ텥??????雅??access token ???熬곣뫖利???
UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        JwtToken newAccessToken = jwtTokenProvider.generateToken(authentication);

        return newAccessToken;
    }

}