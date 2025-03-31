package service.messageservice.service.itf;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    Mono<String> createChat(List<String> participants);
    Flux<ChatRoom> getChats(String username);
}
