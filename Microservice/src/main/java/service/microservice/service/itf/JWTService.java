package service.microservice.service.itf;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

public interface JWTService {
    String generateToken(String username);
    SecretKey getSecretKey();
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    Claims extractAllClaims(String token);
    boolean validateToken(String token, UserDetails userDetails);
    boolean tokenExpired(String token);
    Date extractExpiration(String token);
}
