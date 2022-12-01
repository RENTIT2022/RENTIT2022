package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.MessageResponse;
import kg.neobis.rentit.dto.PermissionDto;
import kg.neobis.rentit.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@Validated
@RequiredArgsConstructor
@Tag(name = "Permission Controller", description = "The Permission API with documentation annotations")
public class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "Get all permissions")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @Operation(summary = "Get permission by id")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<PermissionDto> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @Operation(summary = "Insert permission")
    @PostMapping(produces = "application/json")
    public ResponseEntity<PermissionDto> insertPermission(@Valid @RequestBody PermissionDto permissionDTO) {
        return new ResponseEntity<>(permissionService.insert(permissionDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Update permission by id")
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<PermissionDto> updatePermissionById(@PathVariable Long id,
                                                              @Valid @RequestBody PermissionDto permissionDTO) {
        return ResponseEntity.ok(permissionService.updatePermissionById(id, permissionDTO));
    }

    @Operation(summary = "Delete permission by id")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<MessageResponse> deletePermissionById(@PathVariable Long id) {
        permissionService.deletePermissionById(id);
        return ResponseEntity.ok(new MessageResponse("Deleted permission with id = " + id));
    }
}
