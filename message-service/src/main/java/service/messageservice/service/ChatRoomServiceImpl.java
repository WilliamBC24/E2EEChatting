package service.messageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatRoom;
import service.messageservice.entity.dto.ChatWithKeyDTO;
import service.messageservice.entity.dto.CreateChatDTO;
import service.messageservice.entity.dto.UserDTO;
import service.messageservice.repo.ChatRoomRepo;
import service.messageservice.repo.UserRepo;
import service.messageservice.service.itf.ChatRoomService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepo chatRoomRepo;
    private final UserRepo userRepo;

    public ChatRoomServiceImpl(ChatRoomRepo chatRoomRepo, UserRepo userRepo) {
        this.chatRoomRepo = chatRoomRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Mono<String> createChat(CreateChatDTO participants) {
        return chatRoomRepo.findByParticipants(participants.getParticipants())
                .next()
                .switchIfEmpty(chatRoomRepo.save(ChatRoom.builder()
                        .chatId(UUID.randomUUID().toString()
                                .replace("-", ""))
                        .participants(participants.getParticipants())
                        .build()))
                .map(ChatRoom::getChatId)
                .doOnError(e -> log.error("Error creating chat: {}", String.valueOf(e)));
    }

    @Override
    public Mono<List<ChatWithKeyDTO>> getChats(String username) {
        return chatRoomRepo.findByParticipantsContainingOrderByLastChatDesc(username)
                .flatMap(chatRoom -> {
                    String otherUser = chatRoom.getParticipants().stream()
                            .filter(p -> !p.equals(username))
                            .findFirst()
                            .orElse(null);

                    return userRepo.findByUsername(otherUser)
                            .map(publicKey -> new ChatWithKeyDTO(chatRoom, publicKey.getPublicKey()));
                })
                .collectList()
                .doOnError(e -> log.error("Error getting chats: {}", String.valueOf(e)));
    }

    @Override
    public Mono<Void> updateLastChat(String chatID) {
        return chatRoomRepo.findByChatId(chatID)
                .flatMap(chatRoom -> {
                    chatRoom.setLastChat(new Date());
                    log.info("Updating last chat for chatID: {}", chatID);
                    return chatRoomRepo.save(chatRoom);
                })
                .then();
    }
}
