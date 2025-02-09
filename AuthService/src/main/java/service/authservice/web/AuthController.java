package service.authservice.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.authservice.entity.DTO.UserRegisterDTO;
import service.authservice.entity.User;
import service.authservice.service.RegisterServiceImpl;
import service.authservice.service.UserServiceImpl;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final RegisterServiceImpl registerServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AuthController(RegisterServiceImpl registerServiceImpl, UserServiceImpl userServiceImpl) {
        this.registerServiceImpl = registerServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userServiceImpl.verify(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody @Valid UserRegisterDTO accountDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        registerServiceImpl.register(accountDTO);
        return ResponseEntity.ok("Registered Successfully");
    }
}
