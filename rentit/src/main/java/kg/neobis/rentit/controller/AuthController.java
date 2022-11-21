package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.UserDto;
import kg.neobis.rentit.dto.UserIncompleteRegisterDto;
import kg.neobis.rentit.security.request.LoginRequest;
import kg.neobis.rentit.security.request.TokenRefreshRequest;
import kg.neobis.rentit.security.response.JwtResponse;
import kg.neobis.rentit.service.AuthService;
import kg.neobis.rentit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
@Validated
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "The Auth API with documentation annotations")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "User authentication")
    @PostMapping(value = "/sign-in", produces = "application/json")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @Operation(summary = "User registration incomplete")
    @PostMapping(value = "/sign-up-incomplete", produces = "application/json")
    public ResponseEntity<UserDto> registerUserIncomplete(@Valid @RequestBody UserIncompleteRegisterDto userIncompleteRegisterDTO) {
        return new ResponseEntity<>(userService.registerUserIncomplete(userIncompleteRegisterDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get new access token by refresh token")
    @PostMapping(value = "/refresh-token", produces = "application/json")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }
}
