package app.web.security;

import io.jsonwebtoken.Claims;

public class JwtUtil {
    public static JwtAuthentication createJwtAuthentication(Claims claims) {
        return new JwtAuthentication(claims);
    }
}
