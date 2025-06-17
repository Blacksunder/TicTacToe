package app.web.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class JwtAuthentication implements Authentication {
    private final Claims claims;
    private boolean authenticated = false;

    public JwtAuthentication(Claims claims) {
        this.claims = claims;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return claims.get("role", List.class);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return claims.get("uuid", String.class);
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return claims.get("uuid", String.class);
    }
}
