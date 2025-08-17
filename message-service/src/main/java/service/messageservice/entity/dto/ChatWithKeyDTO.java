package service.messageservice.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import service.messageservice.entity.ChatRoom;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatWithKeyDTO {
    private ChatRoom room;
    private String key;
}
