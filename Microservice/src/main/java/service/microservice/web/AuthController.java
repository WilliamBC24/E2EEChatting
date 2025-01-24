package service.microservice.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import service.microservice.entity.DTO.UserRegisterDTO;
import service.microservice.service.RegisterServiceImpl;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final RegisterServiceImpl registerServiceImpl;

    @Autowired
    public AuthController(RegisterServiceImpl registerServiceImpl) {
        this.registerServiceImpl = registerServiceImpl;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDTO", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerAccount(@ModelAttribute("user") @Valid UserRegisterDTO accountDTO, Errors errors) {
        registerServiceImpl.register(accountDTO, errors);
        if (errors.hasErrors()) {
            return "redirect:/auth/register";
        }
        return "redirect:/auth/login";
    }
}
