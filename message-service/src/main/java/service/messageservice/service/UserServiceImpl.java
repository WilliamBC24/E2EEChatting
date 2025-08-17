package service.messageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.User;
import service.messageservice.entity.dto.UserDTO;
import service.messageservice.entity.dto.UserDTOKey;
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
    public Mono<User> connect(UserDTO userDTO) {
        return userRepo.findByUsername(userDTO.getUsername())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found when trying to connect")))
                .flatMap(u -> {
                    if (u.getStatus() == STATUS.ONLINE) {
                        return Mono.just(u);
                    }
                    u.setStatus(STATUS.ONLINE);
                    return userRepo.save(u);
                })
                .doOnError(e -> log.error("Error connecting: {}", String.valueOf(e)));
    }

    @Override
    public Mono<User> disconnect(UserDTO userDTO) {
        return userRepo.findByUsername(userDTO.getUsername())
                .switchIfEmpty(Mono.error(new RuntimeException("User not found when trying to disconnect")))
                .flatMap(u -> {
                    u.setStatus(STATUS.OFFLINE);
                    return userRepo.save(u);
                })
                .doOnError(e -> log.error("Error disconnecting: {}", String.valueOf(e)));
    }

    @Override
    public Mono<User> addUser(UserDTOKey userDTOKey) {
        return userRepo.existsByUsername(userDTOKey.getUsername())
                .flatMap(exist -> {
                    if (Boolean.TRUE.equals(exist)) {
                        return connect(UserDTO.builder().username(userDTOKey.getUsername()).build());
                    }

                    return userRepo.save(User.builder()
                            .username(userDTOKey.getUsername())
                            .publicKey(userDTOKey.getKey())
                            .status(STATUS.ONLINE)
                            .build());
                });
    }


    @Override
    public Flux<User> findOnlineUsers() {
        return userRepo.findAllByStatus(STATUS.ONLINE);
    }

    @Override
    public Mono<Void> updateUserKey(UserDTOKey userDTOKey) {
        return userRepo.updatePublicKeyByUsername(userDTOKey);
    }
}
