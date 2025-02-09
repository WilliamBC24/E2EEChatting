package service.authservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.authservice.entity.DTO.UserRegisterDTO;

@Controller
@RequestMapping("/")
public class PageController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
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
}
