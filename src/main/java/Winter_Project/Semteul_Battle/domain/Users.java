package Winter_Project.Semteul_Battle.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String university;

    private String major;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private int authority;

    @Lob
    @Column(name = "profile", columnDefinition = "BLOB")
    private byte[] profile;

    private int view;

    // 외래키로 사용하는 경우

    @OneToMany(mappedBy = "users")
    private List<Submit> submits;

    @OneToMany(mappedBy = "users")
    private List<Contestant> contestants;

    @OneToMany(mappedBy = "questioner")
    private List<ContestQuestion> questioner;

    @OneToMany(mappedBy = "answerer")
    private List<ContestQuestion> answerer;

    @OneToMany(mappedBy = "users")
    private List<contestNotice> contestNotices;

    @OneToMany(mappedBy = "users")
    private List<Examiner> Examiners;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
