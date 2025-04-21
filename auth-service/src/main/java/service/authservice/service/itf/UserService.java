package service.authservice.service.itf;

import service.authservice.entity.dto.LoginDTO;

import java.util.Map;

public interface UserService {
    Map<String, String> verify(LoginDTO loginDTO);
}
