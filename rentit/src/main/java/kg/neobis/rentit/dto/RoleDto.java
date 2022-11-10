package kg.neobis.rentit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "RoleDto объект для передачи данных")
public class RoleDto {

    @Schema(description = "ID первичный ключ RoleDTO", example = "1")
    private Long id;

    @Schema(description = "Название роли", example = "ROLE_USER", required = true)
    private String name;

    @Schema(description = "Название права", example = "CREATE_USER", required = true)
    private Set<PermissionDto> permissions = new HashSet<PermissionDto>();
}
