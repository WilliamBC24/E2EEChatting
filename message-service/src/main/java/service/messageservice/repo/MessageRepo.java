package service.messageservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import service.messageservice.entity.Message;

public interface MessageRepo extends ReactiveMongoRepository<Message, String> {
    Flux<Message> findByChatId(String chatId);
}
