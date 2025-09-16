package com.easybus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easybus.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	  List<UserRole> findByUserIdAndDeletedAtIsNull(Long userId);
	    List<UserRole> findByRoleRoleIdAndDeletedAtIsNull(Long roleId);
}
