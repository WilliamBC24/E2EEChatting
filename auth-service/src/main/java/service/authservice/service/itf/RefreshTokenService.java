package service.authservice.service.itf;

import java.util.UUID;

public interface RefreshTokenService {
    String generateRefreshToken(Long userId);
}
