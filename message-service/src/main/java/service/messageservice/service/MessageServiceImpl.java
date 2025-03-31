package service.messageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.Message;
import service.messageservice.repo.MessageRepo;
import service.messageservice.service.itf.MessageService;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepo messageRepo;
    public MessageServiceImpl(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Override
    public Mono<Message> save(Message message) {
        return messageRepo.save(message)
                .doOnError(e -> log.error("Error saving message: {}", String.valueOf(e)));
    }

    @Override
    public Flux<Message> findMessages(String chatId) {
        return messageRepo.findByChatId(chatId).switchIfEmpty(Flux.empty())
                .doOnError(e -> log.error("Error finding chat messages: {}", String.valueOf(e)));
    }
}
