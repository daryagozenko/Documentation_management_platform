package app.gozenko.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email обязателен")
        @Email
        String email,

        @NotBlank(message = "Пароль обязателен")
        String password
) {}
