package com.easybus.service;

import java.util.List;

import com.easybus.entity.Role;

public interface RoleService {
    Role createRole(Long userId, String roleName, List<String> permissions);
    List<Role> getRolesByUser(Long userId);
    Role updateRole(Long roleId, String roleName, List<String>	 permissions);
    void deleteRole(Long roleId);
    List<Role> getAllRoles();
    Role getRoleById(Long roleId);
}
