package app.di;

import app.web.security.AuthorizationService;
import app.web.filter.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class AuthConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthFilter authFilter) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/login", "/register", "/access").permitAll()
                        .anyRequest().authenticated())
        .addFilterAfter(authFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthFilter authFilter(AuthorizationService authorizationService) {
        return new AuthFilter(authorizationService);
    }
}
