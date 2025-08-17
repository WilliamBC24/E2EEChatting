package service.messageservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepo extends ReactiveMongoRepository<ChatRoom, String> {
    Mono<ChatRoom> findByChatId(String id);
    Flux<ChatRoom> findByParticipantsContainingOrderByLastChatDesc(String username);
    Flux<ChatRoom> findByParticipants(List<String> participants);
}
