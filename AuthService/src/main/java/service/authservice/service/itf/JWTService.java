package service.authservice.service.itf;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import service.authservice.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

public interface JWTService {
    String generateToken(String username);
    SecretKey getSecretKey();
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    Claims extractAllClaims(String token);
    boolean validateToken(String token, User user);
    boolean tokenExpired(String token);
    Date extractExpiration(String token);
}
