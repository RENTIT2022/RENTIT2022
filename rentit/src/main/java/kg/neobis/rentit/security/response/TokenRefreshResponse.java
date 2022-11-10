package kg.neobis.rentit.security.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenRefreshResponse {

    private final String tokenType = "Bearer";

    private String accessToken;

    private String refreshToken;
}
