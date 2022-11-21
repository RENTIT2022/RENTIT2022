package kg.neobis.rentit.mapper;

import kg.neobis.rentit.dto.PermissionDto;
import kg.neobis.rentit.entity.Permission;

public class PermissionMapper {

    public static Permission permissionDtoToPermission(PermissionDto permissionDTO) {
        Permission permission = Permission.builder()
                .id(permissionDTO.getId())
                .name(permissionDTO.getName())
                .description(permissionDTO.getDescription())
                .build();

        return permission;
    }

    public static PermissionDto permissionToPermissionDto(Permission permission) {
        PermissionDto permissionDTO = PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .build();

        return permissionDTO;
    }
}
