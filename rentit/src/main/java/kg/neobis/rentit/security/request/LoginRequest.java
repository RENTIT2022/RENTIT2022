package kg.neobis.rentit.security.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Schema(description = "LoginRequest объект для аутентификации")
public class LoginRequest {

    @Schema(description = "Email", example = "example@gmail.com", required = true)
    @NotBlank(message = "Email can't be null or empty")
    @Email
    private String email;

    @Schema(description = "Пароль", example = "rentit2022", required = true)
    @NotBlank(message = "Password field can't be empty")
    @Size(min = 8, message = "Password size should be longer than 8 symbols")
    private String password;
}
