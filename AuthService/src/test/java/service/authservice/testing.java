package service.authservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import service.authservice.config.SecurityConfig;
import service.authservice.entity.Enum.Role;
import service.authservice.entity.PASETOToken;
import service.authservice.service.PASETOServiceImpl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

//@SpringBootTest
public class testing {
    @Test
    void testGoodToken() throws JsonProcessingException {
        PASETOServiceImpl tokenService = new PASETOServiceImpl();
        final Long userId = 1L;
        final List<Role> role = List.of(Role.USER);
        final Instant expiresDate = Instant.now().plus(5, ChronoUnit.MINUTES);

        PASETOToken appToken = new PASETOToken();
        appToken.setUserId(userId);
        appToken.setRoles(role);
        appToken.setExpires(expiresDate);

        Optional<String> optToken = tokenService.encrypt(appToken);
        Assertions.assertTrue(optToken.isPresent());
        String token = optToken.get();
        Assertions.assertNotNull(token);

        Optional<PASETOToken> optAppToken = tokenService.decrypt(token);
        Assertions.assertTrue(optAppToken.isPresent());
        PASETOToken decodedAppToken = optAppToken.get();

        Assertions.assertNotNull(decodedAppToken);
        Assertions.assertEquals(userId, decodedAppToken.getUserId());
        Assertions.assertEquals(role, decodedAppToken.getRoles());
        Assertions.assertEquals(expiresDate, decodedAppToken.getExpires());
    }
}
