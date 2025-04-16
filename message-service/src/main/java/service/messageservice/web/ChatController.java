package service.messageservice.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import service.messageservice.entity.Message;
import service.messageservice.entity.ChatNoti;
import service.messageservice.service.itf.MessageService;

@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    public ChatController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    //Listens at /app/chat
    @MessageMapping("/chat")
    public Mono<Message> processMessage(@Payload Message message) {
       return messageService.save(message)
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
                );
    }
}
