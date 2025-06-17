package app.web.controller;

import app.datasource.model.DatasourceUser;
import app.datasource.model.SignUpRequest;
import app.web.security.AuthorizationService;
import app.web.security.JwtRequest;
import app.web.security.JwtResponse;
import app.web.security.RefreshJwtRequest;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @Autowired
    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        try {
            JwtResponse tokens = authorizationService.authorizeUser(request);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody SignUpRequest request) {
        try {
            boolean isRegistered = authorizationService.registerUser(request);
            if (!isRegistered) {
                return "This login is already used";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "redirect:/login";
    }

    @GetMapping("/register")
    public ResponseEntity<?> register() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("This is the registration page");
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("This is the login page");
    }

    @PostMapping("/access")
    public ResponseEntity<?> updateAccessToken(@RequestBody RefreshJwtRequest request) {
        try {
            JwtResponse tokens = authorizationService.updateAccessToken(request.getRefreshToken());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> updateRefreshToken(@RequestBody RefreshJwtRequest request) {
        try {
            JwtResponse tokens = authorizationService.updateRefreshToken(request.getRefreshToken());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
