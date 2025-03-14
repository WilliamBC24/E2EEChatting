package service.authservice.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import service.authservice.entity.Enum.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class BasicDetailDTO {
    private String username;
    private String email;
    private Set<GrantedAuthority> authorities;
    public BasicDetailDTO(String username, String email, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet());
    }
}
