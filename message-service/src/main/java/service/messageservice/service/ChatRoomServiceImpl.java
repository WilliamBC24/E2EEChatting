package service.messageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatRoom;
import service.messageservice.repo.ChatRoomRepo;
import service.messageservice.service.itf.ChatRoomService;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepo chatRoomRepo;
    public ChatRoomServiceImpl(ChatRoomRepo chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }

    @Override
    public Mono<String> createChat(List<String> participants) {
        return chatRoomRepo.save(ChatRoom.builder()
                                .chatId(UUID.randomUUID().toString())
                                .participants(participants)
                                .build())
                .map(ChatRoom::getChatId)
                .doOnError(e -> log.error("Error creating chat: {}", String.valueOf(e)));
    }

    @Override
    public Flux<ChatRoom> getChats(String username) {
        return chatRoomRepo.findByParticipantsContaining(username)
                .doOnError(e -> log.error("Error getting chats: {}", String.valueOf(e)));
    }
}
