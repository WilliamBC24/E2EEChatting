package service.authservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import service.authservice.entity.enums.Role;
import service.authservice.entity.User;
import service.authservice.repo.UserRepo;

import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;

    public UserDetailServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Set<Role> getRolesByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .getRoles();
    }
}
