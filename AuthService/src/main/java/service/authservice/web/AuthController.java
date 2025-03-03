package service.authservice.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.authservice.entity.DTO.BasicDetailDTO;
import service.authservice.entity.DTO.UserRegisterDTO;
import service.authservice.entity.User;
import service.authservice.repo.UserRepo;
import service.authservice.service.RegisterServiceImpl;
import service.authservice.service.UserServiceImpl;
import service.authservice.utils.CookieUtil;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final RegisterServiceImpl registerServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final UserRepo userRepo;

    @Autowired
    public AuthController(RegisterServiceImpl registerServiceImpl, UserServiceImpl userServiceImpl, UserRepo userRepo) {
        this.registerServiceImpl = registerServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.userRepo = userRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response, HttpSession session) {
        Map<String, String> map = userServiceImpl.verify(user);
        CookieUtil.setCookie(response, "jwt", map.get("jwt"), 30000);
        CookieUtil.setCookie(response, "refresh", map.get("refresh"), 30000);
        return ResponseEntity.ok("OK");
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
