package service.messageservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import service.messageservice.entity.User;
import service.messageservice.service.UserServiceImpl;

@Controller
@RequestMapping("/")
public class UserMapping {
    private final UserServiceImpl userService;
    public UserMapping(UserServiceImpl userService) {
        this.userService = userService;
    }

    //This makes it listen from /app/connect, which is the app destination prefix
    @MessageMapping("/connect")
    //This broadcast to the broker
    @SendTo("/broker/topic")
    public User connect(User user) {
        userService.connect(user);
        return user;
    }

    @MessageMapping("/disconnect")
    @SendTo("/broker/topic")
    public User disconnect(@Payload User user) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<Flux<User>> getUsers() {
        return ResponseEntity.ok(userService.findOnlineUsers());
    }
}
