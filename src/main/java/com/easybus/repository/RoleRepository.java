package com.easybus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easybus.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // To find role by its name
    Optional<Role> findByName(String name);

    // To check if a role already exists by name
    boolean existsByName(String name);
    List<Role> findByUserId(Long userId);
    
}
