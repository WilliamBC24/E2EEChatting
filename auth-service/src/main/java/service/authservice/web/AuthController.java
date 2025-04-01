package service.authservice.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import service.authservice.entity.dto.UserRegisterDTO;
import service.authservice.entity.User;
import service.authservice.service.RegisterServiceImpl;
import service.authservice.service.UserServiceImpl;
import service.authservice.utils.CookieUtil;

import java.util.Map;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final RegisterServiceImpl registerServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AuthController(RegisterServiceImpl registerServiceImpl, UserServiceImpl userServiceImpl) {
        this.registerServiceImpl = registerServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpServletResponse response) {
        Map<String, String> map = userServiceImpl.verify(user);
        CookieUtil.setCookie(response, "jwt", map.get("jwt"), 30000);
        CookieUtil.setCookie(response, "refresh", map.get("refresh"), 30000);
        return ResponseEntity.ok(user.getUsername());
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerAccount(@RequestBody @Valid UserRegisterDTO accountDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        registerServiceImpl.register(accountDTO);
        return ResponseEntity.ok("Registered Successfully");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("OK");
    }
}
