package service.messageservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import service.messageservice.entity.RSA;

public interface RSARepo extends ReactiveMongoRepository<RSA, String> {
}
