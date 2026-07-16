package Winter_Project.Semteul_Battle.domain.user.entity;


import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Column(name = "login_id", nullable = false, unique = true)
    @NotBlank(message = "로그인 아이디는 필수입니다.")
    private String loginId;

    @Column(nullable = false)
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "대학교는 필수입니다.")
    private String university;

    private String major;

    @Column(nullable = false, unique = true)
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @Column(nullable = false)
    private int authority;

    @Column(name = "profile")
    private String profile;

    private int view;


    @OneToMany(mappedBy = "users")
    private List<Submit> submits;

    @OneToMany(mappedBy = "users")
    private List<Contestant> contestants;

    @OneToMany(mappedBy = "questioner")
    private List<ContestQuestion> questioner;

    @OneToMany(mappedBy = "answerer")
    private List<ContestQuestion> answerer;

    @OneToMany(mappedBy = "users")
    private List<ContestNotice> ContestNotices;

    @OneToMany(mappedBy = "users")
    private List<Examiner> Examiners;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(UserRole::toAuthority)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void saveProfileUrl(String fileUrl) {
        this.profile = fileUrl;
    }

    public void changeContestVisibility(boolean visible) {
        this.view = visible ? 1 : 0;
    }

}
