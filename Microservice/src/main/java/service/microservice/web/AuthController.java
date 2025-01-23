package service.microservice.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import service.microservice.entity.DTO.AccountRegisterDTO;
import service.microservice.service.RegisterService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final RegisterService registerService;

    @Autowired
    public AuthController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("accountDTO", new AccountRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerAccount(@ModelAttribute("user") @Valid AccountRegisterDTO accountDTO, Errors errors) {
        registerService.register(accountDTO, errors);
        if (errors.hasErrors()) {
            return "redirect:/auth/register";
        }
        return "redirect:/auth/login";
    }
}
