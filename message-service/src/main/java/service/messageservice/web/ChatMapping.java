package service.messageservice.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.Message;
import service.messageservice.entity.ChatNoti;
import service.messageservice.service.itf.ChatRoomService;
import service.messageservice.service.itf.MessageService;

import java.util.List;

@Controller
public class ChatMapping {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final ChatRoomService chatRoomService;
    public ChatMapping(SimpMessagingTemplate messagingTemplate, MessageService messageService, ChatRoomService chatRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.chatRoomService = chatRoomService;
    }

    //Listens at /app/chat
    @MessageMapping("/chat")
    public void processMessage(@Payload Message message) {
        messageService.save(message)
                .doOnNext(savedMessage ->
                    messagingTemplate.convertAndSend(
                            //For WebSocket subscription, at /dest/chatbox/chatId
                            "/chatbox/" + message.getChatId(),
                            //Send the message to the chat so it can be rendered on screen
                            ChatNoti.builder()
                                    .sender(message.getSender())
                                    .chatId(message.getChatId())
                                    .content(message.getContent())
                                    .build()
                    )
                ).subscribe();
    }

    @GetMapping("/message")
    //Create a chat for participants
    public Mono<ResponseEntity<Void>> getChatID(@RequestParam List<String> participants) {
        return chatRoomService.createChat(participants)
                .map(chatId -> ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, "/message/" + chatId)
                        .build()
                );
    }

    //For fetching messages, only needed once for fetching past messages
    @GetMapping("/message/{chatId}")
    public ResponseEntity<Flux<Message>> getMessages(@PathVariable String chatId) {
        return ResponseEntity.ok(messageService.findMessages(chatId));
    }
}
