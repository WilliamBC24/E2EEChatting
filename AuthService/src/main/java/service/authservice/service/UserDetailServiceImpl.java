package service.authservice.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import service.authservice.entity.Enum.Role;
import service.authservice.entity.User;
import service.authservice.repo.UserRepo;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;

    public UserDetailServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles()
                        .stream()
                        .map(Enum::name)
                        .toArray(String[]::new))
                .build();
    }

    public List<Role> getRolesByUsername(String username) {
        return userRepo.findByUsername(username).getRoles();
    }
}
