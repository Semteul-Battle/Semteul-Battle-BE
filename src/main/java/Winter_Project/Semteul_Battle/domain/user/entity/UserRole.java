package Winter_Project.Semteul_Battle.domain.user.entity;

public enum UserRole {
    USER,
    ADMIN;

    private static final String ROLE_PREFIX = "ROLE_";

    public String toAuthority() {
        return ROLE_PREFIX + name();
    }

    public static String toAuthority(String role) {
        if (role == null || role.isBlank()) {
            return USER.toAuthority();
        }
        return role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;
    }
}
