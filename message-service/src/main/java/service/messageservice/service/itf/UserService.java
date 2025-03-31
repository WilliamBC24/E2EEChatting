package service.messageservice.service.itf;

import reactor.core.publisher.Flux;
import service.messageservice.entity.User;

public interface UserService {
    void connect(User user);
    void disconnect(User user);
    Flux<User> findOnlineUsers(); // Get all online users
}
