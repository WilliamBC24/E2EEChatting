package service.messageservice.service.itf;

import reactor.core.publisher.Mono;
import service.messageservice.entity.dto.ChatWithKeyDTO;
import service.messageservice.entity.dto.CreateChatDTO;

import java.util.List;


public interface ChatRoomService {
    Mono<String> createChat(CreateChatDTO participants);
    Mono<List<ChatWithKeyDTO>> getChats(String username);
    Mono<Void> updateLastChat(String chatID);
}
