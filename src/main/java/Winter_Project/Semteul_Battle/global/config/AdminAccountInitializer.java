package Winter_Project.Semteul_Battle.global.config;

import Winter_Project.Semteul_Battle.domain.user.entity.UserRole;
import Winter_Project.Semteul_Battle.domain.user.entity.Users;
import Winter_Project.Semteul_Battle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_LOGIN_ID:}")
    private String adminLoginId;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Value("${ADMIN_NAME:Admin}")
    private String adminName;

    @Value("${ADMIN_EMAIL:admin@semteul-battle.local}")
    private String adminEmail;

    @Value("${ADMIN_UNIVERSITY:Semteul}")
    private String adminUniversity;

    @Value("${ADMIN_MAJOR:}")
    private String adminMajor;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!StringUtils.hasText(adminLoginId) && !StringUtils.hasText(adminPassword)) {
            return;
        }

        if (!StringUtils.hasText(adminLoginId) || !StringUtils.hasText(adminPassword)) {
            throw new IllegalStateException("ADMIN_LOGIN_ID and ADMIN_PASSWORD must be configured together.");
        }

        if (userRepository.existsByLoginId(adminLoginId)) {
            log.info("admin account already exists: {}", adminLoginId);
            return;
        }

        userRepository.findByEmail(adminEmail).ifPresent(user -> {
            throw new IllegalStateException("ADMIN_EMAIL is already used by another account: " + adminEmail);
        });

        Users admin = Users.builder()
                .loginId(adminLoginId)
                .password(passwordEncoder.encode(adminPassword))
                .name(adminName)
                .email(adminEmail)
                .university(adminUniversity)
                .major(adminMajor)
                .roles(new ArrayList<>(List.of(UserRole.ADMIN.name())))
                .build();

        try {
            userRepository.save(admin);
            log.info("admin account initialized: {}", adminLoginId);
        } catch (DataIntegrityViolationException e) {
            log.info("admin account already initialized by another process: {}", adminLoginId);
        }
    }
}
