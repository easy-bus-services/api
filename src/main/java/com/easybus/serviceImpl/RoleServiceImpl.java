package com.easybus.serviceImpl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.easybus.entity.Role;
import com.easybus.entity.User;
import com.easybus.repository.RoleRepository;
import com.easybus.repository.UserRepository;
import com.easybus.service.RoleService;

@Service

public class RoleServiceImpl implements RoleService {

	private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	public Role createRole(Long userId, String roleName, Set<String> permissions) {
		log.info("Creating role '{}' for userId={} with permissions={}", roleName, userId, permissions);

		User user = userRepository.findById(userId).orElseThrow(() -> {
			log.error("User not found with id={}", userId);
			return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		});

		Role role = new Role();
		role.setUser(user);
		role.setName(roleName);
		role.setPermissionsSet(permissions);

		Role savedRole = roleRepository.save(role);
		log.info("Role created successfully with roleId={}", savedRole.getId());
		return savedRole;
	}

	@Override
	public List<Role> getRolesByUser(Long userId) {
		log.info("Fetching roles for userId={}", userId);
		return roleRepository.findByUserId(userId);
	}

	@Override
	public Role updateRole(Long roleId, String roleName, Set<String> permissions) {
		log.info("Updating roleId={} with roleName='{}' and permissions={}", roleId, roleName, permissions);

		Role role = roleRepository.findById(roleId).orElseThrow(() -> {
			log.error("Role not found with id={}", roleId);
			return new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
		});

		role.setName(roleName);
		role.setPermissionsSet(permissions);
		Role updatedRole = roleRepository.save(role);
		log.info("Role updated successfully: roleId={}", updatedRole.getId());
		return updatedRole;
	}

	@Override
	public void deleteRole(Long roleId) {
		log.info("Deleting role with roleId={}", roleId);
		if (!roleRepository.existsById(roleId)) {
			log.error("Role not found with id={}", roleId);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
		}
		roleRepository.deleteById(roleId);
		log.info("Role deleted successfully: roleId={}", roleId);
	}

	@Override
	public List<Role> getAllRoles() {
		log.info("Fetching all roles");
		return roleRepository.findAll();
	}

	@Override
	public Role getRoleById(Long roleId) {
		log.info("Fetching role by roleId={}", roleId);
		return roleRepository.findById(roleId).orElseThrow(() -> {
			log.error("Role not found with id={}", roleId);
			return new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
		});
	}
}