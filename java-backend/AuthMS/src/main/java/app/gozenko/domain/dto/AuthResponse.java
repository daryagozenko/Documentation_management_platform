package app.gozenko.domain.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn // Время жизни access-токена в секундах
) {}
