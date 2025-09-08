package com.easybus.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.easybus.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
