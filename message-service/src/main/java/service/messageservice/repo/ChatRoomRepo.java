package service.messageservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import service.messageservice.entity.ChatRoom;

public interface ChatRoomRepo extends ReactiveMongoRepository<ChatRoom, String> {
    Flux<ChatRoom> findByParticipantsContaining(String userId);
}
