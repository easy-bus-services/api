package com.easybus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybus.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	
	List<Permission> findByParent(Permission parent);

    List<Permission> findByParentIsNull(); // fetch root permissions
	
}
