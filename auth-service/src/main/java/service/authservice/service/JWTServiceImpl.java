package service.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.authservice.entity.Enum.Role;
import service.authservice.entity.User;
import service.authservice.service.itf.JWTService;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {
//    @Value("${TOKEN_SECRET}")
    private String secret="96294b1e459ef04a8ad5f8b1edc114c6d18114a35605c95668a9bc79ab2009156e93e872b3ff3ef05c329d0face831a4f0e1e90d076c3fa643bb4151b1b26d469346dbafaf88407b920157d28879603e5f41109c6d48fadaf23bf6adfdf1ce2fe2720c06cfd01dab8e0dd7d49330ad87c44b2a30043444a7a214f5cdd23c5097e0172731baf3ac8703b88ee927589e70e43bab268b20a13bd94e6fb027978205ef24a4f23a631ee4c58f9595d2544f9fcf83399248b63cc61942b52d1cac699e1aa931b55970e194779a2e98fee489d54a939658c090ec0611d45e07f1f0132ae8d80e6fda88dfac8ceb00778f221192a54f0913fe2453d309188b342803fe46";
//    @Value("${TOKEN_EXPIRATION}")
    private long expiration=111;
    private final UserDetailServiceImpl userDetailServiceImpl;

    public JWTServiceImpl(UserDetailServiceImpl userDetailServiceImpl) {
        this.userDetailServiceImpl = userDetailServiceImpl;
    }

    @Override
    public String generateToken(String username) {
        return generateTokenWithRoles(username, userDetailServiceImpl.getRolesByUsername(username));
    }

    private String generateTokenWithRoles(String username, Set<Role> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        Date currentDate = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + expiration))
                .signWith(getSecretKey())
                .compact();
    }

    @Override
    public SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    @Override
    public boolean validateToken(String token, User user) {
        final String username = extractClaim(token, Claims::getSubject);
        return (username.equals(user.getUsername()) && !tokenExpired(token));
    }

    @Override
    public boolean tokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
