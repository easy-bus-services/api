package com.easybus.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.easybus.entity.Role;
import com.easybus.repository.RoleRepository;
import com.easybus.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class RoleServiceImpl implements RoleService {
	private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
	private final RoleRepository roleRepository;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Role createRole(String roleName, String createdBy) {
		log.info("Creating role: {}", roleName);

		roleRepository.findByRoleNameAndIsDeletedFalse(roleName).ifPresent(r -> {
			log.warn("Role creation failed: role already exists with name={}", roleName);
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already exists: " + roleName);
		});

		Role role = new Role();
		role.setRoleName(roleName);
		role.setCreatedBy(createdBy);

		Role saved = roleRepository.save(role);
		log.info("Role created successfully id={}, name={}", saved.getRoleId(), saved.getRoleName());
		return saved;
	}

	@Override
	public List<Role> getAllRoles() {
		log.info("Fetching all active roles");
		List<Role> roles = roleRepository.findAllByIsDeletedFalse();
		log.debug("Found {} active roles", roles.size());
		return roles;
	}

	@Override
	public Role getRoleById(Long roleId) {
		log.info("Fetching role by id={}", roleId);
		return roleRepository.findByRoleIdAndIsDeletedFalse(roleId).orElseThrow(() -> {
			log.error("Role not found with id={}", roleId);
			return new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id " + roleId);
		});
	}

	@Override
	public Role updateRole(Long roleId, String roleName, String updatedBy) {
		log.info("Updating role id={} with new name={}", roleId, roleName);

		// Check duplicate role name
		 roleRepository.findByRoleNameAndIsDeletedFalse(roleName).ifPresent(existing -> {
		        if (!existing.getRoleId().equals(roleId)) {
		            log.warn("Update failed: role name {} already exists", roleName);
		            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role name already exists: " + roleName);
		        }
		    });



		Role role = getRoleById(roleId);
		role.setRoleName(roleName);
		role.setUpdatedBy(updatedBy);

		Role updated = roleRepository.save(role);
		log.info("Role updated successfully id={}, newName={}", updated.getRoleId(), updated.getRoleName());
		return updated;
	}

	@Override
	public void deleteRole(Long roleId, String deletedBy) {
		log.info("Soft deleting role id={}", roleId);

		Role role = getRoleById(roleId);

		if (role.getIsDeleted()) {
			log.warn("Role id={} already deleted", roleId);
			throw new ResponseStatusException(HttpStatus.GONE, "Role already deleted");
		}

		role.setIsDeleted(true);
		role.setDeletedAt(LocalDateTime.now());
		role.setDeletedBy(deletedBy);

		roleRepository.save(role);
		log.info("Role soft deleted successfully id={}, deletedBy={}", roleId, deletedBy);
	}
}