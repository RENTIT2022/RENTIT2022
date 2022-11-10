package kg.neobis.rentit.security.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Schema(description = "TokenRefreshRequest объект для получения нового access token'а")
public class TokenRefreshRequest {

    @Schema(description = "Refresh Token", required = true)
    @NotBlank(message = "Refresh token can't be empty")
    private String refreshToken;
}
