package service.authservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.paseto4j.commons.PasetoException;
import org.paseto4j.commons.Version;
import org.paseto4j.version4.Paseto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.authservice.entity.PASETOToken;
import service.authservice.service.itf.PASETOService;

import org.paseto4j.commons.SecretKey;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

@Service
public class PASETOServiceImpl implements PASETOService {
//    @Value("${TOKEN_SECRET}")
    private String secret="WfvKvfSqJRKkGRe54NvNyH9M4HAyHNwd";
    private String footer = "PASETO";

    @Override
    public Optional<String> encrypt(PASETOToken token) throws JsonProcessingException {
        String payload = getJsonMapper().writeValueAsString(token);
        return Optional.of(Paseto.encrypt(getSecretKey(), payload, footer));
    }

    @Override
    public Optional<PASETOToken> decrypt(String token) throws JsonProcessingException {
        String payload = Paseto.decrypt(getSecretKey(), token, footer);
        PASETOToken pasetoToken = getJsonMapper().readValue(payload, PASETOToken.class);
        if(Instant.now().isAfter(pasetoToken.getExpires())) {
            return Optional.empty();
        }
        return Optional.of(pasetoToken);
    }

    @Override
    public SecretKey getSecretKey() {
        return new SecretKey(this.secret.getBytes(StandardCharsets.UTF_8), Version.V4);
    }

    @Override
    public JsonMapper getJsonMapper() {
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.findAndRegisterModules();
        return jsonMapper;
    }
}
