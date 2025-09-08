package com.easybus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybus.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
