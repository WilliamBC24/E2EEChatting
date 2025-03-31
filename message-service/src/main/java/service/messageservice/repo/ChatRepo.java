package service.messageservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import service.messageservice.entity.Chat;

public interface ChatRepo extends ReactiveMongoRepository<Chat, String> {
    Flux<Chat> findByChatId(String chatId);
}
