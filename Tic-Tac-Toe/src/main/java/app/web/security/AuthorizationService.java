package app.web.security;

import app.datasource.model.DatasourceUser;
import app.datasource.model.SignUpRequest;
import app.datasource.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthorizationService(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    public boolean registerUser(SignUpRequest request) {
        try {
            return userService.registerUser(request);
        } catch (Exception e) {
            // ignored
        }
        return false;
    }

    public JwtResponse authorizeUser(JwtRequest request) {
        DatasourceUser user = userService.authorizeUser(request);
        if (user != null) {
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);
            return new JwtResponse(accessToken, refreshToken);
        }
        return null;
    }

    public JwtResponse updateAccessToken(String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getClaims(refreshToken);
            String uuid = claims.get("uuid", String.class);
            DatasourceUser user = userService.findUserByUuid(uuid);
            if (user != null) {
                String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            } else {
                throw new AuthException("User not found");
            }
        }
        throw new AuthException("Invalid token");
    }

    public JwtResponse updateRefreshToken(String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getClaims(refreshToken);
            String uuid = claims.getId();
            DatasourceUser user = userService.findUserByUuid(uuid);
            if (user != null) {
                String accessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                return new JwtResponse(accessToken, newRefreshToken);
            } else {
                throw new AuthException("User not found");
            }
        }
        throw new AuthException("Invalid token");
    }

    private JwtAuthentication createAuthentication(Claims claims) {
        return JwtUtil.createJwtAuthentication(claims);
    }

    public UserService getUserService() {
        return userService;
    }

    public JwtProvider getJwtProvider() {
        return jwtProvider;
    }

    public void addClaimsToSecurityContext(Claims claims) {
        JwtAuthentication authentication = createAuthentication(claims);
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
