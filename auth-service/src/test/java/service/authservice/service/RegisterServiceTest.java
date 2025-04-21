package service.authservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import service.authservice.entity.enums.Role;
import service.authservice.entity.User;
import service.authservice.entity.dto.RegisterDTO;
import service.authservice.repo.UserRepo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @InjectMocks
    RegisterServiceImpl registerService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepo userRepo;

    @Test
    void registerSuccess() {
        RegisterDTO userRegisterDTO = new RegisterDTO(
                "username", "pass", "pass", "email@email.com");

        when(passwordEncoder.encode("pass")).thenReturn("hashedPass");
        when(userRepo.existsByUsername("username")).thenReturn(false);
        when(userRepo.existsByEmail("email@email.com")).thenReturn(false);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        doNothing().when(userRepo).save(any());

        registerService.register(userRegisterDTO);

        verify(userRepo).existsByUsername("username");
        verify(userRepo).existsByEmail("email@email.com");
        verify(passwordEncoder).encode("pass");
        verify(userRepo).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser, "User should not be null");
        assertEquals("username", savedUser.getUsername());
        assertEquals("hashedPass", savedUser.getPassword());
        assertEquals("email@email.com", savedUser.getEmail());
        assertTrue(savedUser.getRoles().contains(Role.USER));
    }

    @Test
    void registerFailsWhenUsernameExists() {
        RegisterDTO registerDTO = new RegisterDTO(
                "username", "pass", "pass", "email@email.com");

        when(userRepo.existsByUsername("username")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> registerService.register(registerDTO),
                "Expected register() to throw when username exists"
        );

        assertEquals("Username is already in use", exception.getMessage());

        verify(userRepo, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void registerFailsWhenEmailExists() {
        RegisterDTO registerDTO = new RegisterDTO(
                "username", "pass", "pass", "email@email.com");

        when(userRepo.existsByUsername("username")).thenReturn(false);
        when(userRepo.existsByEmail("email@email.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> registerService.register(registerDTO),
                "Expected register() to throw when email exists"
        );

        assertEquals("Email is already in use", exception.getMessage());

        verify(userRepo, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }
}
