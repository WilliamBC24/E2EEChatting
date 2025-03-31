package service.messageservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//This is used to display message
public class ChatNoti {
    private String id;
    private String sender;
    private String receiver;
    private String content;
}
