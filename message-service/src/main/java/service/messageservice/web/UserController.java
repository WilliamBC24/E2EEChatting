package service.messageservice.web;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import service.messageservice.entity.ChatRoom;
import service.messageservice.entity.User;
import service.messageservice.entity.dto.ChatWithKeyDTO;
import service.messageservice.entity.dto.UserDTO;
import service.messageservice.entity.dto.UserDTOKey;
import service.messageservice.entity.response.ApiResponse;
import service.messageservice.service.ChatRoomServiceImpl;
import service.messageservice.service.UserServiceImpl;
import service.messageservice.util.ResponseBuilder;

import java.util.List;
import java.util.Map;

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
    public Mono<ResponseEntity<ApiResponse<UserDTO>>> connect(@RequestBody UserDTO userDTO) {
        return userService.connect(userDTO)
                .map(connectedUser -> ResponseBuilder.buildSuccessResponse("Connect success", UserDTO.builder()
                        .username(connectedUser.getUsername()).build()));
    }

    @PutMapping("/disconnect")
    public Mono<ResponseEntity<ApiResponse<User>>> disconnect(@RequestBody UserDTO userDTO) {
        return userService.disconnect(userDTO)
                .map(user -> ResponseBuilder.buildSuccessResponse("Disconnect success", user));
    }

    @PutMapping("/add")
    public Mono<ResponseEntity<ApiResponse<User>>> add(@RequestBody UserDTOKey userDTOKey) {
        return userService.addUser(userDTOKey)
                .map(user -> ResponseBuilder.buildSuccessResponse("Added user", user));
    }

    @PutMapping("/updateKey")
    public Mono<ResponseEntity<ApiResponse<String>>> updateKey(@RequestBody UserDTOKey userDTOKey) {
        return userService.updateUserKey(userDTOKey)
                .then(Mono.just(ResponseBuilder.buildSuccessResponse("Updated key", "Yeah")));
    }

    @GetMapping("/users")
    public Mono<ResponseEntity<ApiResponse<List<User>>>> getUsers() {
        return userService.findOnlineUsers()
                .collectList()
                .map(onlineUsers -> ResponseBuilder.buildSuccessResponse("Got online users", onlineUsers));
    }

    @PostMapping("/rooms")
    public Mono<ResponseEntity<ApiResponse<List<ChatWithKeyDTO>>>> getRooms(@RequestBody UserDTO userDTO) {
        return chatRoomService.getChats(userDTO.getUsername())
                .map(map -> ResponseBuilder.buildSuccessResponse("Got chatrooms and keys", map));
    }
}
