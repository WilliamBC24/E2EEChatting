package service.messageservice.service.itf;

import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatParticipants;

public interface ChatRoomService {
    Mono<String> getChatRoomId(String sender, String receiver, boolean newRoom);
    Mono<String> createChat(String sender, String receiver);
    Mono<ChatParticipants> getChatParticipants(String chatId);
}
