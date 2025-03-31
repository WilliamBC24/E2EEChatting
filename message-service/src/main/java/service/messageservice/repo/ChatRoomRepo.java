package service.messageservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatRoom;

public interface ChatRoomRepo extends ReactiveMongoRepository<ChatRoom, String> {
    Mono<ChatRoom> findBySenderAndReceiver(String sender, String receiver);
    Mono<ChatRoom> findByChatId(String chatId);
}
