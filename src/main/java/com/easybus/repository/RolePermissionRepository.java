package com.easybus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybus.entity.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
	  Optional<RolePermission> findByRoleId(Long roleId);
}