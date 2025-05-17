package service.messageservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatRoom;
import service.messageservice.entity.User;
import service.messageservice.entity.dto.UserDTO;
import service.messageservice.entity.response.ApiResponse;
import service.messageservice.service.ChatRoomServiceImpl;
import service.messageservice.service.UserServiceImpl;
import service.messageservice.util.ResponseBuilder;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;
    private final ChatRoomServiceImpl chatRoomService;

    public UserController(UserServiceImpl userService, ChatRoomServiceImpl chatRoomService) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
    }

    @PutMapping("/connect")
    public Mono<ResponseEntity<ApiResponse<List<ChatRoom>>>> connect(@RequestBody UserDTO userDTO) {
        return userService.connect(userDTO)
                .flatMap(connectedUser -> chatRoomService.getChats(connectedUser.getUsername())
                        .collectList())
                .map(chatRooms -> ResponseBuilder.buildSuccessResponse("Connect success", chatRooms));
    }

    @PutMapping("/disconnect")
    public Mono<ResponseEntity<ApiResponse<User>>> disconnect(@RequestBody UserDTO userDTO) {
        return userService.disconnect(userDTO)
                .map(user -> ResponseBuilder.buildSuccessResponse("Disconnect success", user));
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<ApiResponse<User>>> add(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO)
                .map(user -> ResponseBuilder.buildSuccessResponse("Added user", user));
    }

    @GetMapping("/users")
    public Mono<ResponseEntity<ApiResponse<List<User>>>> getUsers() {
        return userService.findOnlineUsers()
                .collectList()
                .map(onlineUsers -> ResponseBuilder.buildSuccessResponse("Got online users", onlineUsers));
    }
}
