package service.authservice.service.itf;

import service.authservice.entity.DTO.UserRegisterDTO;

public interface RegisterService {
    void register(UserRegisterDTO user);
}
