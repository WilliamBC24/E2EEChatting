package service.messageservice.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import service.messageservice.entity.Message;
import service.messageservice.entity.ChatNoti;
import service.messageservice.service.itf.ChatRoomService;
import service.messageservice.service.itf.MessageService;

@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final ChatRoomService chatRoomService;
    private RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public ChatController(SimpMessagingTemplate messagingTemplate, MessageService messageService, ChatRoomService chatRoomService, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.chatRoomService = chatRoomService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @MessageMapping("/chat")
    public Mono<Message> processMessage(@Payload Message message) {
        String topic = "chatbox:" + message.getChatId(); // Redis topic

        return messageService.save(message)
                .flatMap(savedMessage -> {
                    try {
                        // Serialize ChatNoti to JSON string
                        String json = objectMapper.writeValueAsString(
                                ChatNoti.builder()
                                        .sender(savedMessage.getSender())
                                        .chatId(savedMessage.getChatId())
                                        .content(savedMessage.getContent())
                                        .nonce(savedMessage.getNonce())
                                        .build()
                        );
                        // Publish to Redis
                        redisTemplate.convertAndSend(topic, json);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    return chatRoomService.updateLastChat(savedMessage.getChatId())
                            .thenReturn(savedMessage);
                });
    }

}
