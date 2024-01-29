package Winter_Project.Semteul_Battle.service.Users;


import Winter_Project.Semteul_Battle.domain.Users;
import Winter_Project.Semteul_Battle.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return userRepository.findByLoginId(loginId).map(this::createUserDetails).orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetails(Users users) {
        String encodedPassword = users.getPassword();

        // 이미 해싱되지 않은 비밀번호라면, 해싱을 수행
        if (!encodedPassword.startsWith("$2a$")) {
            encodedPassword = passwordEncoder.encode(users.getPassword());
        }

        return User.builder().username(users.getLoginId()).password(encodedPassword).roles(users.getRoles().toArray(new String[0])).build();
    }
}