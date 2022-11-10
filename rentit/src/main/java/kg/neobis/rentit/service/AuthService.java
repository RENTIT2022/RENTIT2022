package kg.neobis.rentit.service;

import io.jsonwebtoken.Claims;
import kg.neobis.rentit.entity.User;
import kg.neobis.rentit.mapper.UserMapper;
import kg.neobis.rentit.security.jwt.JwtProvider;
import kg.neobis.rentit.security.request.LoginRequest;
import kg.neobis.rentit.security.response.JwtResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;
    private final UserService userService;

    @SneakyThrows
    public JwtResponse authenticateUser(@NonNull LoginRequest loginRequest) {
        User user = UserMapper.userDtoToUser(userService.getUserByEmail(loginRequest.getEmail()));

        if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);

            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .middleName(user.getMiddleName())
                    .dateOfBirth(user.getDateOfBirth())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getRole().getName())
                    .isRegistrationComplete(user.isRegistrationComplete())
                    .isVerifiedByTechSupport(user.isVerifiedByTechSupport())
                    .status(user.getStatus())
                    .imageUser(user.getImageUser())
                    .build();

        } else {
            throw new AuthException("Неверный пароль для email: " + loginRequest.getEmail());
        }
    }

    @SneakyThrows
    public JwtResponse refreshToken(@NonNull String refreshToken) {
        if (jwtProvider.validateToken(refreshToken)) {
            final Claims claims = jwtProvider.getClaims(refreshToken);
            final String email = claims.getSubject();
            User user = UserMapper.userDtoToUser(userService.getUserByEmail(email));
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String newRefreshToken = jwtProvider.generateRefreshToken(user);

            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken)
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .middleName(user.getMiddleName())
                    .dateOfBirth(user.getDateOfBirth())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getRole().getName())
                    .isRegistrationComplete(user.isRegistrationComplete())
                    .isVerifiedByTechSupport(user.isVerifiedByTechSupport())
                    .status(user.getStatus())
                    .imageUser(user.getImageUser())
                    .build();
        }
        throw new AuthException("Invalid Refresh Token [" + refreshToken + "]");
    }
}
