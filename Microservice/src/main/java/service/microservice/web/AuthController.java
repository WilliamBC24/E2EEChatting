package service.microservice.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import service.microservice.entity.DTO.UserRegisterDTO;
import service.microservice.entity.User;
import service.microservice.service.RegisterServiceImpl;
import service.microservice.service.UserServiceImpl;

@RestController
@RequestMapping("/auth")
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
    public String registerAccount(@RequestBody @Valid UserRegisterDTO accountDTO, Errors errors) {
        registerServiceImpl.register(accountDTO, errors);
        if (errors.hasErrors()) {
            return "redirect:/auth/register";
        }
        return "redirect:/auth/login";
    }
}
