package service.authservice.service.itf;

import service.authservice.entity.dto.RegisterDTO;

public interface RegisterService {
    void register(RegisterDTO user);
}
