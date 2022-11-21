package kg.neobis.rentit.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import kg.neobis.rentit.entity.ImageUser;
import kg.neobis.rentit.enums.AuthProvider;
import kg.neobis.rentit.enums.Status;
import lombok.*;

import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "UserDto объект для передачи данных")
public class UserDto {

    @Schema(description = "ID первичный ключ UserDTO", example = "1")
    private Long id;

    @Schema(description = "Имя", example = "Джон")
    private String firstName;

    @Schema(description = "Фамилия", example = "Дои")
    private String lastName;

    @Schema(description = "Отчество", example = "Смит")
    private String middleName;

    @Schema(description = "Дата рождения",example = "2000-01-01")
    private LocalDate dateOfBirth;

    @Schema(description = "Паспортные данные")
    private PassportDataDto passportData;

    @Schema(description = "Адрес по прописке")
    private RegisteredAddressDto registeredAddress;

    @Schema(description = "Адрес по местопроживанию")
    private ResidenceAddressDto residenceAddress;

    @Schema(description = "Email", example = "example@gmail.com")
    @Email(message = "Email is invalid")
    private String email;

    @Schema(description = "Номер телефона", example = "+996700777777")
    private String phoneNumber;

    @Schema(description = "Роль пользователя")
    private RoleDto role;

    @Schema(description = "Пароль")
    @JsonIgnore
    private String password;

    @Schema(description = "Пользователь зарегитрирован полностью")
    private boolean isRegistrationComplete;

    @Schema(description = "Пользователь верифицирован Тех. поддержкой")
    private boolean isVerifiedByTechSupport;

    @Schema(description = "Статус пользователя")
    private Status status;

    @Schema(description = "Фотки пользователя")
    private List<ImageUser> imageUser;

    @JsonIgnore
    private AuthProvider provider;

    @JsonIgnore
    private String providerId;

    @JsonIgnore
    private Map<String, Object> attributes;

    @JsonIgnore
    private Integer resetPasswordCode;

    @JsonIgnore
    private Instant codeExpirationDate;
}
