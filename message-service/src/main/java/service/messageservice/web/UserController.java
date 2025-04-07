package service.messageservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatRoom;
import service.messageservice.entity.User;
import service.messageservice.entity.dto.UserDTO;
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
    public Mono<List<ChatRoom>> connect(@RequestBody UserDTO userDTO) {
        return userService.connect(userDTO)
                .flatMap(connectedUser -> chatRoomService.getChats(connectedUser.getUsername())
                        .collectList());
    }

    @PutMapping("/disconnect")
    public Mono<ResponseEntity<User>> disconnect(@RequestBody UserDTO userDTO) {
        return userService.disconnect(userDTO)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<User>> add(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/users")
    public Mono<ResponseEntity<List<User>>> getUsers() {
        return userService.findOnlineUsers()
                .collectList()
                .map(ResponseEntity::ok);
    }
}
