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
                .map(chatRooms -> ResponseEntity.ok(ApiResponse.<List<ChatRoom>>builder()
                        .success(true)
                        .message("Connect success")
                        .data(chatRooms)
                        .build()));
    }

    @PutMapping("/disconnect")
    public Mono<ResponseEntity<ApiResponse<User>>> disconnect(@RequestBody UserDTO userDTO) {
        return userService.disconnect(userDTO)
                .map(user -> ResponseEntity.ok(ApiResponse.<User>builder()
                        .success(true)
                        .message("Disconnect success")
                        .data(user)
                        .build()));
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<ApiResponse<User>>> add(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO)
                .map(user -> ResponseEntity.ok(ApiResponse.<User>builder()
                        .success(true)
                        .message("Add success")
                        .data(user)
                        .build()));
    }

    @GetMapping("/users")
    public Mono<ResponseEntity<ApiResponse<List<User>>>> getUsers() {
        return userService.findOnlineUsers()
                .collectList()
                .map(onlineUsers -> ResponseEntity.ok(ApiResponse.<List<User>>builder()
                        .success(true)
                        .message("Got online users")
                        .data(onlineUsers)
                        .build()));
    }
}
