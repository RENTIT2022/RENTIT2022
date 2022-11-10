package kg.neobis.rentit.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import kg.neobis.rentit.entity.User;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private Long jwtExpirationMs;

    @Value("${jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    public String generateAccessToken(@NonNull User user) {
        Date now = new Date();
        final Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .withClaim("roles", user.getRole().getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("user_id", user.getId())
                .sign(algorithm);
    }

    public String generateAccessToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Date now = new Date();
        final Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .withClaim("roles", user.getRole().getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("user_id", user.getId())
                .sign(algorithm);
    }

    public String generateRefreshToken(@NonNull User user) {
        Date now = new Date();
        final Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(now)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenDurationMs))
                .withClaim("roles", String.valueOf((new SimpleGrantedAuthority("can:refresh"))))
                .sign(algorithm);
    }

    public boolean validateToken(@NonNull String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    public Claims getClaims(@NonNull String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
