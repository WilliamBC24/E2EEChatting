package service.messageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.Chat;
import service.messageservice.repo.ChatRepo;
import service.messageservice.service.itf.ChatRoomService;
import service.messageservice.service.itf.ChatService;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final ChatRepo chatRepo;
    private final ChatRoomService chatRoomService;

    public ChatServiceImpl(ChatRepo chatRepo, ChatRoomService chatRoomService) {
        this.chatRepo = chatRepo;
        this.chatRoomService = chatRoomService;
    }

    @Override
    public Mono<Chat> save(Chat chat) {
        return chatRoomService.getChatParticipants(chat.getChatId())
                .flatMap(participants -> {
                    chat.setReceiver(participants.getOtherParticipant(chat.getSender()));
                    return chatRepo.save(chat);
                })
                .doOnError(e -> log.error("Error saving chat message: {}", String.valueOf(e)));
    }


    @Override
    public Flux<Chat> findChat(String chatId) {
        return chatRepo.findByChatId(chatId).switchIfEmpty(Flux.empty())
                .doOnError(e -> log.error("Error finding chat messages: {}", String.valueOf(e)));
    }
}
