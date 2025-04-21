package service.authservice.web;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.authservice.entity.dto.LoginDTO;
import service.authservice.entity.dto.RegisterDTO;
import service.authservice.entity.response.ApiResponse;
import service.authservice.entity.response.LoginResponse;
import service.authservice.entity.response.RegisterResponse;
import service.authservice.service.RegisterServiceImpl;
import service.authservice.service.UserServiceImpl;
import service.authservice.utils.CookieUtil;

import java.util.Map;

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
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        Map<String, String> map = userServiceImpl.verify(loginDTO);
        CookieUtil.setCookie(response, "jwt", map.get("jwt"), 30000);
        CookieUtil.setCookie(response, "refresh", map.get("refresh"), 30000);
        return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login success")
                .data(new LoginResponse(loginDTO.getUsername()))
                .build());
    }

    @PostMapping("/register")
    //BindingResult is the result of validation, in case you want to return it
    public ResponseEntity<ApiResponse<RegisterResponse>> registerAccount(@RequestBody @Valid RegisterDTO accountDTO) {
        //@Valid will throw an error which will be handled with global helper
        registerServiceImpl.register(accountDTO);
        return ResponseEntity.ok(ApiResponse.<RegisterResponse>builder()
                .success(true)
                .message("Register success")
                .data(new RegisterResponse(accountDTO.getUsername()))
                .build());
    }
}
