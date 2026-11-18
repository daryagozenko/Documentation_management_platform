package app.gozenko.controller;

import app.gozenko.domain.dto.AuthResponse;
import app.gozenko.domain.dto.LoginRequest;
import app.gozenko.domain.dto.RegisterRequest;
import app.gozenko.domain.entity.User;
import app.gozenko.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        User registeredUser = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", registeredUser.getId(),
                        "email", registeredUser.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
