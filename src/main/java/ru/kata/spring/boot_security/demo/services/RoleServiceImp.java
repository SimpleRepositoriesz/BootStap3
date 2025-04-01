package ru.kata.spring.boot_security.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImp implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> getRoles() {
        return new HashSet<>(roleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByName(String roleUser) {
        return roleRepository.findByName(roleUser);
    }

    @Override
    @Transactional
    public void save(Role roles) {
        roleRepository.save(roles);
    }


}