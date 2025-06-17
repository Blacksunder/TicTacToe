package app.web.security;

import app.datasource.model.DatasourceUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtProvider {
    private final SecretKey jwtSecret;

    public JwtProvider(@Value("${jwt.secret.access})") String jwtSecret) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateAccessToken(DatasourceUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getUuid());
        claims.put("role", user.getRoles());
        return Jwts.builder().claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(jwtSecret)
                .compact();
    }

    public String generateRefreshToken(DatasourceUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", user.getUuid());
        return Jwts.builder().claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(jwtSecret)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, jwtSecret);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, jwtSecret);
    }

    private boolean validateToken(String token, SecretKey secret) {
        try {
            Jwts.parser().verifyWith(secret).build().
                    parseSignedClaims(token).getPayload();
            return true;
        } catch (Exception e) {
            System.out.println("invalid token, Error:" + e.getMessage());
        }
        return false;
    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(jwtSecret).build().
                parseSignedClaims(token).getPayload();
    }
}
