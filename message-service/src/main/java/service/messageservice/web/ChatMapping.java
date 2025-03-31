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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.Chat;
import service.messageservice.entity.ChatNoti;
import service.messageservice.service.itf.ChatRoomService;
import service.messageservice.service.itf.ChatService;

@Controller
public class ChatMapping {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    public ChatMapping(SimpMessagingTemplate messagingTemplate, ChatService chatService, ChatRoomService chatRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.chatRoomService = chatRoomService;
    }

    //Listens at /app/chat
    @MessageMapping("/chat")
    public void processMessage(@Payload Chat chat) {
        chatService.save(chat)
                .doOnNext(savedChat -> {
                    String receiver = savedChat.getReceiver();
                    messagingTemplate.convertAndSendToUser(
                            receiver,
                            //For WebSocket subscription, at /dest/chatbox/chatId
                            "/chatbox/" + chat.getChatId(),
                            //Send the message to the receiver so it can be rendered on screen
                            ChatNoti.builder()
                                    .id(chat.getId())
                                    .sender(chat.getSender())
                                    .receiver(receiver)
                                    .content(chat.getMessage())
                                    .build()
                    );
                }).subscribe();
    }


    @GetMapping("/message/{sender}/{receiver}")
    public Mono<ResponseEntity<Void>> getChatID(@PathVariable String sender, @PathVariable String receiver) {
        //Get chatId and redirect to a better looking link, also to not have to worry about order
        return chatRoomService.getChatRoomId(sender, receiver, true)
                .map(chatId -> ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, "/message/" + chatId)
                        .build()
                );
    }


    //For fetching messages, only needed once for fetching past messages
    @GetMapping("/message/{chatId}")
    public ResponseEntity<Flux<Chat>> getMessages(@PathVariable String chatId) {
        return ResponseEntity.ok(chatService.findChat(chatId));
    }
}
