package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PassportDataDto объект для передачи данных")
public class PassportDataDto {

    @Schema(description = "ID первичный ключ PassportDataDto", example = "1")
    private Long id;

    @Schema(description = "ИНН", example = "11111111111111", required = true)
    @Size(min = 14, max = 14, message = "Password size should be  14 symbols")
    private String tin;

    @Schema(description = "Дата выдачи", example = "2000-01-01", required = true)
    private LocalDate dateOfIssue;

    @Schema(description = "Орган выдачи", example = "МКК 000000", required = true)
    @NotBlank(message = "Authority can't be null or empty")
    private String authority;
}
