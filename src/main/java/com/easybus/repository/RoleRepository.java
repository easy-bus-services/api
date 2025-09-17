package com.easybus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybus.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

//    // To find role by its name
//    Optional<Role> findByName(String name);
//
//    // To check if a role already exists by name
//    boolean existsByName(String name);
//    List<Role> findByUserId(Long userId);
//    /////
    ///
    
    // Only fetch roles not soft-deleted
	 Optional<Role> findByRoleIdAndIsDeletedFalse(Long roleId);
//
	 Optional<Role> findByRoleNameAndIsDeletedFalse(String roleName);
	//    List<Role> findAllByIsDeletedFalse();

	//    boolean existsByRoleNameAndIsDeletedFalse(String roleName);
    
	    
	    // Only fetch roles not soft-deleted
//		 Optional<Role> findByRoleIdAndIsDeletedFalse(Long roleId);

		    List<Role> findAllByIsDeletedFalse();

		    boolean existsByRoleNameAndIsDeletedFalse(String roleName);
		    
//		    
//
//		    @Query("SELECT r FROM Role r JOIN FETCH r.permissions p WHERE r.roleId = :roleId AND r.isDeleted = false")
//		    Optional<Role> findActiveRoleWithPermissions(@Param("roleId") Long roleId);
		    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions p WHERE r.roleId = :roleId AND r.isDeleted = false")
		    Optional<Role> findActiveRoleWithPermissions(@Param("roleId") Long roleId);
}
