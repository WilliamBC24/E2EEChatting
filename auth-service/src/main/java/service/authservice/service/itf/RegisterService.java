package service.authservice.service.itf;

import service.authservice.entity.dto.UserRegisterDTO;

public interface RegisterService {
    void register(UserRegisterDTO user);
}
