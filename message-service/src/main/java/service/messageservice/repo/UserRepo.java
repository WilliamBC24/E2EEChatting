package service.messageservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.User;
import service.messageservice.entity.dto.UserDTOKey;
import service.messageservice.entity.enums.STATUS;
import service.messageservice.service.itf.UserRepoExtension;

public interface UserRepo extends ReactiveMongoRepository<User, String>, UserRepoExtension {
    Flux<User> findAllByStatus(STATUS status);
    Mono<User> findByUsername(String username);
    Mono<Boolean> existsByUsername(String name);
}
