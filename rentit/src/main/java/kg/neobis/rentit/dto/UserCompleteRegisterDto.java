package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "UserCompleteRegisterDto объект для полной регистрации пользователя")
public class UserCompleteRegisterDto {

    @Schema(description = "ID первичный ключ UserDTO", example = "1", required = true)
    private Long id;

    @Schema(description = "Имя", example = "Джон", required = true)
    @NotBlank(message = "First name can't be null or empty")
    private String firstName;

    @Schema(description = "Фамилия", example = "Дои", required = true)
    @NotBlank(message = "Last name can't be null or empty")
    private String lastName;

    @Schema(description = "Номер телефона", example = "+996777777777", required = true)
    @NotBlank(message = "Phone Number can't be null or empty")
    private String phoneNumber;

    @Schema(description = "Дата рождения", example = "2000-01-01", required = true)
    private LocalDate dateOfBirth;

    @Schema(description = "Паспортные данные", required = true)
    @Valid
    private PassportDataDto passportData;

    @Schema(description = "Адрес по прописке", required = true)
    @Valid
    private RegisteredAddressDto registeredAddress;

    @Schema(description = "Адрес по местопроживанию", required = true)
    @Valid
    private ResidenceAddressDto residenceAddress;
}
