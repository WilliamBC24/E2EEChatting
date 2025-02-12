package service.authservice.entity;

import lombok.Data;
import service.authservice.entity.Enum.Role;

import java.time.Instant;
import java.util.List;

@Data
public class PASETOToken {
    private Long userId;
    private List<Role> roles;
    private Instant expires;
}
