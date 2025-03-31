package service.messageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.User;
import service.messageservice.entity.enums.STATUS;
import service.messageservice.repo.UserRepo;
import service.messageservice.service.itf.UserService;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void connect(User user) {
        userRepo.findByUsername(user.getUsername())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(u -> {
                    u.setStatus(STATUS.ONLINE);
                    return userRepo.save(u);
                })
                .doOnError(e -> log.error("Error connecting: {}", String.valueOf(e)))
                .subscribe();
    }

    @Override
    public void disconnect(User user) {
        userRepo.findByUsername(user.getUsername())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(u -> {
                    u.setStatus(STATUS.OFFLINE);
                    return userRepo.save(u);
                })
                .doOnError(e -> log.error("Error disconnecting: {}", String.valueOf(e)))
                .subscribe();
    }

    @Override
    public Flux<User> findOnlineUsers() {
        return userRepo.findAllByStatus(STATUS.ONLINE);
    }
}
