package com.easybus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybus.entity.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

//    // Check if a role-permission mapping exists
//    boolean existsByRoleAndPermission(Role role, Permission permission);
//
//    // Find a specific mapping
//    Optional<RolePermission> findByRole_IdAndPermission_Id(Long roleId, Long permissionId);
//
//    // Find all permissions for a role
//    List<RolePermission> findByRole_Id(Long roleId);	
}