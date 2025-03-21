package viet.DACN.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import viet.DACN.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
} 
