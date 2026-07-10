package Winter_Project.Semteul_Battle.domain.user.service;


import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
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
        return userRepository.findByLoginId(loginId).map(this::createUserDetails).orElseThrow(() -> new UsernameNotFoundException("???????汝뷴젆??녷뉩??읂????????饔낅떽???????????????깅즽????????놁졄."));
    }

    // ???????汝뷴젆??녷뉩??읂?User ???????????? ???怨쀫뮡??????꿔꺂??????믊삳룛?UserDetails ????ル늉????????蹂κ텤???饔낅떽????????????닿튃癲?return
private UserDetails createUserDetails(Users users) {
        String encodedPassword = users.getPassword();

        // ???? ??????깅즽???? ??? ?????癲???????嚥▲굧?먩뤆?? ??????깅즽?????????壤?
if (!encodedPassword.startsWith("$2a$")) {
            encodedPassword = passwordEncoder.encode(users.getPassword());
        }

        return User.builder().username(users.getLoginId()).password(encodedPassword).roles(users.getRoles().toArray(new String[0])).build();
    }
}