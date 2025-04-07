package service.messageservice.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import service.messageservice.entity.Message;
import service.messageservice.service.ChatRoomServiceImpl;
import service.messageservice.service.MessageServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final ChatRoomServiceImpl chatRoomService;
    private final MessageServiceImpl messageService;
    public MessageController(ChatRoomServiceImpl chatRoomService, MessageServiceImpl messageService) {
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
    }

    @PostMapping("/createChat")
    //Create a chat for participants
    public Mono<ResponseEntity<Void>> getChatID(@RequestParam List<String> participants) {
        return chatRoomService.createChat(participants)
                .map(chatId -> ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, "/message/" + chatId)
                        .build()
                );
    }

    //For fetching messages, only needed once for fetching past messages
    @GetMapping("/{chatId}")
    public Mono<ResponseEntity<List<Message>>> getMessages(@PathVariable String chatId) {
        return messageService.findMessages(chatId)
                .collectList()
                .map(ResponseEntity::ok);
    }
}
