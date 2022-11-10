package kg.neobis.rentit.security.response;

import kg.neobis.rentit.entity.ImageUser;
import kg.neobis.rentit.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    private String role;
    private boolean isRegistrationComplete;
    private boolean isVerifiedByTechSupport;
    private Status status;
    private List<ImageUser> imageUser;
}
