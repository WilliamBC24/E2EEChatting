package service.authservice.service;

import org.springframework.stereotype.Service;
import service.authservice.service.itf.RefreshTokenService;

import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Override
    public String generateRefreshToken(Long userId) {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
