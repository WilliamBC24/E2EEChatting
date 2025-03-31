package service.messageservice.service.itf;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.Message;

public interface MessageService {
    Mono<Message> save(Message message);
    Flux<Message> findMessages(String chatId);
}
