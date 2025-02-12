package service.authservice.service.itf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import service.authservice.entity.PASETOToken;

import org.paseto4j.commons.SecretKey;
import java.util.Optional;

public interface PASETOService {
    Optional<String> encrypt(PASETOToken token) throws JsonProcessingException;
    Optional<PASETOToken> decrypt(String token) throws JsonProcessingException;
    SecretKey getSecretKey();
    JsonMapper getJsonMapper();
}
