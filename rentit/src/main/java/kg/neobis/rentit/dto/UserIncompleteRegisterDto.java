package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "UserIncompleteRegisterDTO объект для неполной регистрации пользователя")
public class UserIncompleteRegisterDto {

    @Schema(description = "Имя", example = "Джон", required = true)
    @NotBlank(message = "First name field can't be null!")
    private String firstName;

    @Schema(description = "Email", example = "example@gmail.com", required = true)
    @NotBlank(message = "Email field can't be null!")
    @Email
    private String email;

    @Schema(description = "Пароль", example = "rentit2022", required = true)
    @NotBlank(message = "Password field can't be null!")
    @Size(min = 8, message = "Password size should be longer than 8 symbols")
    private String password;
}
