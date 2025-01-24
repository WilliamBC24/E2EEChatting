package service.microservice.service.itf;

import org.springframework.validation.Errors;
import service.microservice.entity.DTO.UserRegisterDTO;

public interface RegisterService {
    void register(UserRegisterDTO user, Errors errors);
}
