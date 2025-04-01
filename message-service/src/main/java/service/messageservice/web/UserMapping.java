package service.messageservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatRoom;
import service.messageservice.entity.User;
import service.messageservice.service.ChatRoomServiceImpl;
import service.messageservice.service.UserServiceImpl;

import java.util.List;

@RestController
public class UserMapping {
    private final UserServiceImpl userService;
    private final ChatRoomServiceImpl chatRoomService;
    public UserMapping(UserServiceImpl userService, ChatRoomServiceImpl chatRoomService) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/user.connect")
    public Mono<List<ChatRoom>> connect(@RequestBody User user) {
        userService.connect(user);
        return chatRoomService.getChats(user.getUsername())
                .collectList();
    }

    @PostMapping("/user.disconnect")
    public User disconnect(@RequestBody User user) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<Flux<User>> getUsers() {
        return ResponseEntity.ok(userService.findOnlineUsers());
    }
}
