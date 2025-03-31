package service.messageservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.User;
import service.messageservice.entity.enums.STATUS;

public interface UserRepo extends ReactiveMongoRepository<User, String> {
    Flux<User> findAllByStatus(STATUS status);
    Mono<User> findByUsername(String username);
}
