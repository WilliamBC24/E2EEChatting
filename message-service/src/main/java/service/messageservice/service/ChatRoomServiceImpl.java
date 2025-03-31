package service.messageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatParticipants;
import service.messageservice.entity.ChatRoom;
import service.messageservice.repo.ChatRoomRepo;
import service.messageservice.service.itf.ChatRoomService;

import java.util.UUID;

@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepo chatRoomRepo;
    public ChatRoomServiceImpl(ChatRoomRepo chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }

    @Override
    public Mono<String> getChatRoomId(String sender, String receiver, boolean newRoom) {
        String user1 = sender.compareTo(receiver) < 0 ? sender : receiver;
        String user2 = sender.compareTo(receiver) < 0 ? receiver : sender;
        return chatRoomRepo.findBySenderAndReceiver(user1, user2)
                //.map will always return a Mono or Flux, as it applies a function to the value inside Mono or Flux
                //then wrap it back again with the same type (Mono or Flux)
                //So use .flatMap when the function inside returns a Mono or Flux
                .map(ChatRoom::getChatId)
                .switchIfEmpty(
                    newRoom ? createChat(sender, receiver) : Mono.empty()
                )
                .doOnError(e -> log.error("Error getting chat room: {}", String.valueOf(e)));
    }

    @Override
    public Mono<String> createChat(String sender, String receiver) {
        return chatRoomRepo.save(ChatRoom.builder()
                                .chatId(UUID.randomUUID().toString())
                                .sender(sender)
                                .receiver(receiver)
                                .build()).map(ChatRoom::getChatId)
                .doOnError(e -> log.error("Error creating chat: {}", String.valueOf(e)));
    }

    @Override
    public Mono<ChatParticipants> getChatParticipants(String chatId) {
        return chatRoomRepo.findByChatId(chatId)
                .map(chatRoom -> new ChatParticipants(chatRoom.getSender(), chatRoom.getReceiver()))
                .doOnError(e -> log.error("Error getting chat participants: {}", String.valueOf(e)));
    }
}
