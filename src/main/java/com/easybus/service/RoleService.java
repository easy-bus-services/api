package com.easybus.service;

import java.util.List;

import com.easybus.entity.Role;

public interface RoleService {


//    Role createRole(Long userId, String roleName, List<String> permissions);
//    List<Role> getRolesByUser(Long userId);
//    Role updateRole(Long roleId, String roleName, List<String>	 permissions);
//    void deleteRole(Long roleId);
//    List<Role> getAllRoles();
//    Role getRoleById(Long roleId);

	Role createRole(String roleName, String createdBy);

	List<Role> getAllRoles();

	Role getRoleById(Long id);

	Role updateRole(Long id, String roleName, String updatedBy);
	void deleteRole(Long id, String deletedBy);
}
