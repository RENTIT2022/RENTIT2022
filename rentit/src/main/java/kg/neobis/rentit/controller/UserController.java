package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.*;
import kg.neobis.rentit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "The User API with documentation annotations")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('UPDATE_USER')")
    @Operation(summary = "Get all users")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasAuthority('ROLE_TECH_SUPPORT') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all not verified users")
    @GetMapping(value = "/not-verified", produces = "application/json")
    public ResponseEntity<List<UserDto>> getAllNotVerifiedUsers() {
        return ResponseEntity.ok(userService.getAllNotVerifiedUsers());
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Get user by id")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasAuthority('ROLE_TECH_SUPPORT') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Accepting user")
    @PutMapping(value = "/accept-user/{id}", produces = "application/json")
    public ResponseEntity<UserDto> acceptUser(@PathVariable Long id) throws MessagingException {
        return new ResponseEntity<>(userService.acceptUser(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_TECH_SUPPORT') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Rejecting user")
    @PutMapping(value = "/reject-user/{id}", produces = "application/json")
    public ResponseEntity<UserDto> rejectUser(@PathVariable Long id) throws MessagingException {
        return new ResponseEntity<>(userService.rejectUser(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "User registration complete separately parameter")
    @PutMapping(value = "/sign-up-complete-parameter", produces = "application/json")
    public ResponseEntity<UserDto> registerUserComplete(@Valid @RequestBody UserCompleteRegisterDto userCompleteRegisterDto) {
        return new ResponseEntity<>(userService.registerUserCompleteParameter(userCompleteRegisterDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "User registration complete separately files")
    @PutMapping(value = "/sign-up-complete-files/{id}", produces = "application/json")
    public ResponseEntity<UserDto> registerUserComplete(@PathVariable Long id,
                                                        @RequestPart MultipartFile[] multipartFiles) {
        return new ResponseEntity<>(userService.registerUserCompleteFiles(id, multipartFiles), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "User registration complete")
    @PutMapping(value = "/sign-up-complete", produces = "application/json")
    public ResponseEntity<UserDto> registerUserComplete(@Valid @RequestPart UserCompleteRegisterDto userCompleteRegisterDto,
                                                        @RequestPart MultipartFile[] multipartFiles) {
        return new ResponseEntity<>(userService.registerUserComplete(userCompleteRegisterDto, multipartFiles), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Operation(summary = "Update password")
    @PutMapping(value = "/update-password", produces = "application/json")
    public ResponseEntity<MessageResponse> updateUserPassword(@Valid @RequestBody ChangePasswordDto changePasswordDTO) {
        return ResponseEntity.ok(userService.updatePassword(changePasswordDTO));
    }

    @PreAuthorize("hasAuthority('ROLE_TECH_SUPPORT') or hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Delete user by id")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<MessageResponse> deleteUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

    @Operation(summary = "Get user profile")
    @DeleteMapping(value = "/get-profile", produces = "application/json")
    public ResponseEntity<UserProfileDto> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }
}
