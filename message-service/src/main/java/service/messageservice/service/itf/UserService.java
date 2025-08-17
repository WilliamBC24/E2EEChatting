package service.messageservice.service.itf;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.User;
import service.messageservice.entity.dto.UserDTO;
import service.messageservice.entity.dto.UserDTOKey;

public interface UserService {
    Mono<User> connect(UserDTO userDTO);
    Mono<User> disconnect(UserDTO userDTO);
    Mono<User> addUser(UserDTOKey userDTOKey);
    Flux<User> findOnlineUsers(); // Get all online users
    Mono<Void> updateUserKey(UserDTOKey userDTOKey);
}
