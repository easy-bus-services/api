package com.easybus.service;

import java.util.List;

import com.easybus.entity.Permission;

public interface PermissionService {

	Permission createPermission(Permission permission);

	List<Permission> createPermissions(List<Permission> permissions);

	List<Permission> updatePermissions(List<Permission> permissions);

	void deletePermission(Long id);

	void deletePermissions(List<Long> ids);

	List<Permission> getAllPermissions();

	Permission getPermissionById(Long id);

	void assignPermissionToRole(Long roleId, Long permissionId);

	void assignPermissionsToRole(Long roleId, List<Long> permissionIds);

	void removePermissionFromRole(Long roleId, Long permissionId);

	void removePermissionsFromRole(Long roleId, List<Long> permissionIds);

	Permission updatePermission(Long id, Permission permission);

}
