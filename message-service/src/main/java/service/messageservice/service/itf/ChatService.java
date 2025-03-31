package service.messageservice.service.itf;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.Chat;

public interface ChatService {
    Mono<Chat> save(Chat chat);
    Flux<Chat> findChat(String chatId);
}
