package app.web.filter;

import app.web.security.AuthorizationService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

@Component
public class AuthFilter extends GenericFilterBean {

    private final AuthorizationService authorizationService;
    private final static List<String> PUBLIC_POINTS =
            List.of("/login", "/register", "/access");

    @Autowired
    public AuthFilter(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (PUBLIC_POINTS.stream().anyMatch(path -> path.matches(request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getTokenFromRequest(request);
        if (token != null && authorizationService.getJwtProvider().validateAccessToken(token)) {
            Claims claims = authorizationService.getJwtProvider().getClaims(token);
            authorizationService.addClaimsToSecurityContext(claims);
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(401);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
