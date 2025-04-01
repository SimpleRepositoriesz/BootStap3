package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.GrantedAuthority;
import ru.kata.spring.boot_security.demo.entities.Role;

public class GrantedAuthorityImp implements GrantedAuthority {
    private final Role role;

    public GrantedAuthorityImp(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getRoles();
    }

    public Role getRole() {
        return role;
    }
}