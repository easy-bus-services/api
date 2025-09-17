package com.easybus.service;

import java.util.List;
import java.util.Set;

import com.easybus.entity.Permission;

public interface PermissionService {

	
	// CRUD
    Permission createPermission(Permission permission);
    List<Permission> createPermissions(List<Permission> permissions);

    Permission updatePermission(Long id, Permission permission);
    List<Permission> updatePermissions(List<Permission> permissions);

    void deletePermission(Long id);
    void deletePermissions(List<Long> ids);

    List<Permission> getAllPermissions();
    Permission getPermissionById(Long id);
    // Assign/remove
	Set<Permission> getPermissionsByRole(Long roleId);
	List<Long> assignPermissionsToRole(Long roleId, List<Long> permissionIds);
	List<Long> removePermissionsFromRole(Long roleId, List<Long> permissionIds);
	

    // Assign/remove
  //  void assignPermissionToRole(Long roleId, Long permissionId);
  //  void assignPermissionsToRole(Long roleId, List<Long> permissionIds);

//   void removePermissionFromRole(Long roleId, Long permissionId);
//    void removePermissionsFromRole(Long roleId, List<Long> permissionIds);
//	void assignPermissionToRole(Long roleId, List<Long> permissionId);
//	void removePermissionsFromCsv(Long roleId, String permissionIdsCsv);
//	void assignPermissionsFromCsv(Long roleId, String permissionIdsCsv);

}
