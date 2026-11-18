package app.gozenko.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Email обязателен для заполнения")
        @Email(message = "Некорректный формат email")
        String email,

        @NotBlank(message = "Пароль обязателен для заполнения")
        @Size(min = 8, max = 100, message = "Пароль должен содержать от 8 до 100 символов")
        String password

) {}