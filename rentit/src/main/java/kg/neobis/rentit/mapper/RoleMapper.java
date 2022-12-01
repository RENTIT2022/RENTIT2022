package kg.neobis.rentit.mapper;

import kg.neobis.rentit.dto.PermissionDto;
import kg.neobis.rentit.dto.RoleDto;
import kg.neobis.rentit.entity.Permission;
import kg.neobis.rentit.entity.Role;

import java.util.HashSet;
import java.util.Set;

public class RoleMapper {

    public static Role roleDtoToRole(RoleDto roleDTO) {
        Set<Permission> permissions = new HashSet<Permission>();
        if(roleDTO.getPermissions().size() != 0) {
            for (PermissionDto permissionDTO : roleDTO.getPermissions()) {
                permissions.add(PermissionMapper.permissionDtoToPermission(permissionDTO));
            }
        }

        Role role = Role.builder()
                .id(roleDTO.getId())
                .name(roleDTO.getName())
                .permissions(permissions)
                .build();

        return role;
    }

    public static RoleDto roleToRoleDto(Role role) {
        Set<PermissionDto> permissionDtos = new HashSet<PermissionDto>();
        if(role.getPermissions().size() != 0) {
            for (Permission permission : role.getPermissions()) {
                permissionDtos.add(PermissionMapper.permissionToPermissionDto(permission));
            }
        }

        RoleDto roleDTO = RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .permissions(permissionDtos)
                .build();

        return roleDTO;
    }
}
