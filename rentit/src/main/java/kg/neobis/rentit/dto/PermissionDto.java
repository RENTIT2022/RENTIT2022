package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PermissionDto объект для передачи данных")
public class PermissionDto {

    @Schema(description = "ID первичный ключ PermissionDTO", example = "1")
    private Long id;

    @Schema(description = "Название права", example = "CREATE_USER", required = true)
    private String name;

    @Schema(description = "Описание права", example = "Роли с этим правом могут добавлять новых пользователей", required = true)
    private String description;
}
