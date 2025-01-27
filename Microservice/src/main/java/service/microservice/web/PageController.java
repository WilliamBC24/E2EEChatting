package service.microservice.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.microservice.entity.DTO.UserRegisterDTO;

@Controller
@RequestMapping("/")
public class PageController {
    @GetMapping("/")
    public String home(Model model) {
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
