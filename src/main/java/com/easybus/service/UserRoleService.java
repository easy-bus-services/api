package com.easybus.service;

import java.util.List;

import com.easybus.entity.Role;
import com.easybus.entity.UserRole;

public interface UserRoleService {

	UserRole assignRoleToUser(Long userId, Long roleId);

	void removeRoleFromUser(Long userRoleId);

	List<Role> getRolesByUser(Long userId);

	

}
