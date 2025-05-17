package service.messageservice.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import service.messageservice.entity.Message;
import service.messageservice.entity.response.ApiResponse;
import service.messageservice.service.ChatRoomServiceImpl;
import service.messageservice.service.MessageServiceImpl;
import service.messageservice.util.ResponseBuilder;

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

    //For fetching messages
    //Use flux stream and pagination instead, the data will be sent in chunks
    //On the client side it will still be 1 json and can still be used for rendering
    //Or if you dont want it in chunks, render page by page and send request for more
    @GetMapping("/{chatId}")
    public Mono<ResponseEntity<ApiResponse<List<Message>>>> getMessages(@PathVariable String chatId) {
        return messageService.findMessages(chatId)
                .collectList()
                .map(messages -> ResponseBuilder.buildSuccessResponse("Got messages list", messages));
    }
}
