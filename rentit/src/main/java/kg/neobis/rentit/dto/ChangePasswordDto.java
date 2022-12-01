package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "ChangePasswordDto объект для смены пароля")
public class ChangePasswordDto {

    @Schema(description = "ID первичный ключ User", example = "1", required = true)
    private Long userId;

    @Schema(description = "Старый пароль", example = "")
    private String oldPassword;

    @Schema(description = "Новый пароль", example = "rentit2022", required = true)
    @Size(min = 8, message = "Пароль должен быть больше 8 символов!")
    private String newPassword;
}
