package Winter_Project.Semteul_Battle.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "profile")
    private String profile;

    private int view;

    // 외래키로 사용하는 경우

    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Submit> submits;

    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Contestant> contestants;

    @JsonIgnore
    @OneToMany(mappedBy = "questioner")
    private List<ContestQuestion> questioner;

    @JsonIgnore
    @OneToMany(mappedBy = "answerer")
    private List<ContestQuestion> answerer;

    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<ContestNotice> ContestNotices;

    @JsonIgnore
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
        return String.valueOf(this.id); // 기본키(id) 반환
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

    public void saveProfileUrl(String fileUrl) {
        this.profile = fileUrl;
    }


}
