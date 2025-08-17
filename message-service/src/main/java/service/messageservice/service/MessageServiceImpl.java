package service.messageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatRoom;
import service.messageservice.entity.Message;
import service.messageservice.repo.ChatRoomRepo;
import service.messageservice.repo.MessageRepo;
import service.messageservice.service.itf.MessageService;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepo messageRepo;
    private final ChatRoomRepo chatRoomRepo;
    public MessageServiceImpl(MessageRepo messageRepo, ChatRoomRepo chatRoomRepo) {
        this.messageRepo = messageRepo;
        this.chatRoomRepo = chatRoomRepo;
    }

    @Override
    public Mono<Message> save(Message message) {
        return messageRepo.save(message)
                .flatMap(savedMessage ->
                        chatRoomRepo.findByChatId(savedMessage.getChatId())
                                .flatMap(chat -> {
                                    chat.setLastChat(savedMessage.getTimestamp());
                                    return chatRoomRepo.save(chat);
                                })
                                .thenReturn(savedMessage)
                );
    }

    @Override
    public Flux<Message> findMessages(String chatId) {
        return messageRepo.findByChatId(chatId).switchIfEmpty(Flux.empty())
                .doOnError(e -> log.error("Error finding chat messages: {}", String.valueOf(e)));
    }
}
