package com.easybus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybus.entity.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
  
}