package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "RegisteredAddressDto объект для передачи данных")
public class RegisteredAddressDto {

    @Schema(description = "ID первичный ключ RegisteredAddressDto", example = "1")
    private Long id;

    @Schema(description = "Область", example = "Чуйская область", required = true)
    @NotBlank(message = "Region can't be null or empty")
    private String region;

    @Schema(description = "Город/Село", example = "Бишкек", required = true)
    @NotBlank(message = "CityOrVillage can't be null or empty")
    private String cityOrVillage;

    @Schema(description = "Район", example = "Ленинский", required = true)
    @NotBlank(message = "District can't be null or empty")
    private String district;

    @Schema(description = "Улица", example = "Токтогула", required = true)
    @NotBlank(message = "Street can't be null or empty")
    private String street;

    @Schema(description = "№ дома", example = "1")
    private int houseNumber;

    @Schema(description = "№ квартиры", example = "1")
    private int apartmentNumber;
}
