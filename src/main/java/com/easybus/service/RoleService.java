package com.easybus.service;

import java.util.List;
import java.util.Set;

import com.easybus.entity.Role;

public interface RoleService {
    Role createRole(Long userId, String roleName, Set<String> permissions);
    List<Role> getRolesByUser(Long userId);
    Role updateRole(Long roleId, String roleName, Set<String> permissions);
    void deleteRole(Long roleId);
    List<Role> getAllRoles();
    Role getRoleById(Long roleId);
}
