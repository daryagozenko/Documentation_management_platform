package app.gozenko.service;

import app.gozenko.domain.dto.AuthResponse;
import app.gozenko.domain.dto.LoginRequest;
import app.gozenko.domain.dto.RegisterRequest;
import app.gozenko.domain.entity.Role;
import app.gozenko.domain.entity.User;
import app.gozenko.domain.repository.RoleRepository;
import app.gozenko.domain.repository.UserRepository;
import app.gozenko.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new UserAlreadyExistsException("Пользователь с email " +
                    registerRequest.email() + " уже существует");
        }

        User user = new User();
        user.setEmail(registerRequest.email());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.password()));

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("роль USER не найдена"));

        user.addRole(role);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("Неверные данные"));

        if(!user.getIsActive()) {
            throw new BadCredentialsException("Аккаунт заблокирован");
        }

        if(!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Неверный пароль");
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        // TODO: Здесь мы сохраним refreshToken в Redis

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                900
        );

    }
}
