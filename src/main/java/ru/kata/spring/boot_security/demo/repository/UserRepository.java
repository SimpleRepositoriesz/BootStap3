package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);


    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findWithRolesByUsername(String username);


    @EntityGraph(attributePaths = {"roles", "email"})
    Optional<User> findWithRolesAndEmailById(Long id);
}
