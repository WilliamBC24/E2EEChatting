package service.authservice.service.itf;

import service.authservice.entity.DTO.BasicDetailDTO;
import service.authservice.entity.User;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    Map<String, String> verify(User user);
    Optional<BasicDetailDTO> getBasicDetails(String username);
}
