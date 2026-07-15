package Winter_Project.Semteul_Battle.domain.user.entity;


import Winter_Project.Semteul_Battle.domain.contest.entity.*;
import Winter_Project.Semteul_Battle.domain.problem.entity.*;
import Winter_Project.Semteul_Battle.domain.menu.entity.*;
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


}
