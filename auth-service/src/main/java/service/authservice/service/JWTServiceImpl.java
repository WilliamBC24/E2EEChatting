package service.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.authservice.entity.enums.Role;
import service.authservice.entity.User;
import service.authservice.service.itf.JWTService;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {
    @Value("${TOKEN_SECRET}")
    private String secret;
    @Value("${TOKEN_EXPIRATION}")
    private long expiration;
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
