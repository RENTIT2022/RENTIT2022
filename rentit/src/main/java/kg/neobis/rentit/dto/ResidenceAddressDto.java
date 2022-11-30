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
@Schema(description = "ResidenceAddressDto объект для передачи данных")
public class ResidenceAddressDto {

    @Schema(description = "ID первичный ключ ResidenceAddressDto", example = "1")
    private Long id;

    @Schema(description = "Область", example = "Чуйская область", required = true)
    @NotBlank(message = "Область не должна быть пустой")
    private String region;

    @Schema(description = "Город/Село", example = "Бишкек", required = true)
    @NotBlank(message = "Город/Село не должно быть пустым")
    private String cityOrVillage;

    @Schema(description = "Район", example = "Ленинский", required = true)
    @NotBlank(message = "Район не должен быть пустым")
    private String district;

    @Schema(description = "Улица", example = "Токтогула", required = true)
    @NotBlank(message = "Улица не должна быть пустой")
    private String street;

    @Schema(description = "№ дома", example = "1")
    private int houseNumber;

    @Schema(description = "№ квартиры", example = "1")
    private int apartmentNumber;
}
