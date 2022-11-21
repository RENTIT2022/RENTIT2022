package kg.neobis.rentit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.neobis.rentit.dto.MessageResponse;
import kg.neobis.rentit.security.response.JwtResponse;
import kg.neobis.rentit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/")
@Validated
@RequiredArgsConstructor
@Tag(name = "Forgot Password Controller", description = "The Forgot Password API with documentation annotations")
public class ForgotPasswordController {

    private final UserService userService;

    @Operation(summary = "Sending code to reset password")
    @PostMapping(value = "/send-reset-code", produces = "application/json")
    public ResponseEntity<MessageResponse> sendResetPasswordCode(@NotBlank(message = "The email field can't be null")
                                                                     @Email @RequestParam String email) throws MessagingException {
        return ResponseEntity.ok(userService.sendResetPasswordCode(email));
    }

    @Operation(summary = "Checking confirmation code to reset password")
    @PostMapping(value = "/check-reset-code", produces = "application/json")
    public ResponseEntity<JwtResponse> checkResetPasswordCode(@RequestParam Integer code) {
        return ResponseEntity.ok(userService.verifyResetPasswordCodeExpirationDate(code));
    }
}
