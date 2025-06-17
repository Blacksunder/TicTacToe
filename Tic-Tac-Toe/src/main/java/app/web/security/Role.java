package app.web.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER("ROLE_USER");

    private final String vale;

    Role(String vale) {
        this.vale = vale;
    }

    @Override
    public String getAuthority() {
        return vale;
    }
}