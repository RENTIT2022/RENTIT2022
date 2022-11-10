package kg.neobis.rentit.service;

import kg.neobis.rentit.dto.PermissionDto;
import kg.neobis.rentit.entity.Permission;
import kg.neobis.rentit.exception.ResourceNotFoundException;
import kg.neobis.rentit.mapper.PermissionMapper;
import kg.neobis.rentit.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<PermissionDto> getAllPermissions() {
        Iterable<Permission> permissions = permissionRepository.findAll();
        List<PermissionDto> permissionDtos = new ArrayList<PermissionDto>();

        for (Permission permission : permissions){
            permissionDtos.add(PermissionMapper.permissionToPermissionDto(permission));
        }

        return permissionDtos;
    }

    public PermissionDto getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find permission by id = " + id));

        return PermissionMapper.permissionToPermissionDto(permission);
    }

    public PermissionDto insert(PermissionDto permissionDTO) {
        Permission permission = Permission.builder()
                .name(permissionDTO.getName())
                .description(permissionDTO.getDescription())
                .build();

        return PermissionMapper.permissionToPermissionDto(permissionRepository.save(permission));
    }

    public PermissionDto updatePermissionById(Long id, PermissionDto permissionDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find permission by id = " + id));

        permission.setName(permissionDTO.getName());
        permission.setDescription(permission.getDescription());

        return PermissionMapper.permissionToPermissionDto(permissionRepository.save(permission));
    }

    public void deletePermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find permission by id = " + id));

        permissionRepository.delete(permission);
    }
}
