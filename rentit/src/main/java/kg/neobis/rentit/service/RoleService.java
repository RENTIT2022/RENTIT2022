package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.MyEntry;
import kg.neobis.rentit.dto.PermissionDto;
import kg.neobis.rentit.dto.RoleDto;
import kg.neobis.rentit.dto.RolePermissionDto;
import kg.neobis.rentit.entity.Permission;
import kg.neobis.rentit.entity.Role;
import kg.neobis.rentit.exception.AlreadyExistException;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.mapper.PermissionMapper;
import kg.neobis.rentit.mapper.RoleMapper;
import kg.neobis.rentit.repository.PermissionRepository;
import kg.neobis.rentit.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    public HashMap<String, Map<String, String>> getRolesPermissions() {
        HashMap<String, Map<String, String>> map = new HashMap<>();

        roleRepository.findAll()
                .forEach(
                        r -> map.put(
                                r.getName(),
                                r.getPermissions()
                                        .stream()
                                        .collect(
                                                Collectors.toMap
                                                        (Permission::getName, Permission::getDescription)
                                        )
                        )
                );

        return map;
    }

    public RoleDto getRoleByName(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find role with name = " + roleName));

        return RoleMapper.roleToRoleDto(role);
    }

    public RoleDto createRole(RoleDto roleDTO) {
        if (roleRepository.existsByName(roleDTO.getName())) {
            throw new AlreadyExistException("Role with the given name already exists.");
        }

        Role role = new Role();
        role.setName(roleDTO.getName());

        return RoleMapper.roleToRoleDto(roleRepository.save(role));
    }

    public PermissionDto createPermission(PermissionDto permissionDTO) {
        if (permissionRepository.existsByName(permissionDTO.getName())) {
            throw new AlreadyExistException("Permission with the given name already exists.");
        }

        Permission permission = new Permission();
        permission.setName(permissionDTO.getName());
        permission.setDescription(permissionDTO.getDescription());

        return PermissionMapper.permissionToPermissionDto(permissionRepository.save(permission));
    }

    public RolePermissionDto addPermissionToRole(RolePermissionDto rolePermissionDTO) {
        Role role = roleRepository.findByName(rolePermissionDTO.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find role with name: " +
                        rolePermissionDTO.getRole()));

        Permission permission = permissionRepository.findByName(rolePermissionDTO.getPermission())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find permission with name: " +
                        rolePermissionDTO.getPermission()));

        Set<Permission> permissions = role.getPermissions();

        permissions.add(permission);

        roleRepository.save(role);

        return rolePermissionDTO;
    }

    public MyEntry<String, Object> deletePermissionFromRole(RolePermissionDto rolePermissionDTO) {
        Role role = roleRepository.findByName(rolePermissionDTO.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find role with name: " +
                        rolePermissionDTO.getRole()));

        Permission permission = permissionRepository.findByName(rolePermissionDTO.getPermission())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find permission with name: " +
                        rolePermissionDTO.getPermission()));

        Set<Permission> permissions = role.getPermissions();

        permissions.remove(permission);

        roleRepository.save(role);

        return new MyEntry<>(
                role.getName(),
                role.getPermissions()
                        .stream()
                        .collect(Collectors.toMap
                                (Permission::getName, Permission::getDescription)
                        )
        );
    }
}
