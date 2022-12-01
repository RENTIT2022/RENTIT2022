package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.MyEntry;
import kg.neobis.rentit.dto.PermissionDto;
import kg.neobis.rentit.dto.RoleDto;
import kg.neobis.rentit.dto.RolePermissionDto;
import kg.neobis.rentit.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@Validated
@RequiredArgsConstructor
@Tag(name = "Role Controller", description = "The Role API with documentation annotations")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Get roles with permissions")
    @GetMapping(produces = "application/json")
    public ResponseEntity<HashMap<String, Map<String, String>>> getRolesPermissions() {
        return ResponseEntity.ok(roleService.getRolesPermissions());
    }

    @Operation(summary = "Create new role")
    @PostMapping(value = "/create-role", produces = "application/json")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDTO) {
        return ResponseEntity.ok(roleService.createRole(roleDTO));
    }

    @Operation(summary = "Create new permission")
    @PostMapping(value = "/create-permission", produces = "application/json")
    public ResponseEntity<PermissionDto> createPermission(@RequestBody PermissionDto permissionDTO) {
        return ResponseEntity.ok(roleService.createPermission(permissionDTO));
    }

    @Operation(summary = "Adding permission to role")
    @PostMapping(value = "/add-permission-to-role", produces = "application/json")
    public ResponseEntity<RolePermissionDto> addPermissionToRole(@RequestBody RolePermissionDto rolePermissionDTO) {
        return ResponseEntity.ok(roleService.addPermissionToRole(rolePermissionDTO));
    }

    @Operation(summary = "Removing permission to role")
    @PutMapping("/delete-permission-from-role")
    public ResponseEntity<MyEntry<String, Object>> deletePermissionFromRole(@RequestBody RolePermissionDto rolePermissionDTO) {
        return ResponseEntity.ok(roleService.deletePermissionFromRole(rolePermissionDTO));
    }
}
