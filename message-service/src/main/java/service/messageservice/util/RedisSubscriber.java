package service.messageservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import service.messageservice.entity.ChatNoti;

@Component
public class RedisSubscriber {
    private SimpMessagingTemplate messagingTemplate;
    private ObjectMapper objectMapper;

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    public void onMessage(String message, String pattern) {
        try {
            ChatNoti msg = objectMapper.readValue(message, ChatNoti.class);
            messagingTemplate.convertAndSend("/chatbox/" + msg.getChatId(), msg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
