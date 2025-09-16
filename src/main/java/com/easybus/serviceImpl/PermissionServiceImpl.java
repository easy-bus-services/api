package com.easybus.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easybus.entity.Permission;
import com.easybus.entity.Role;
import com.easybus.entity.RolePermission;
import com.easybus.repository.PermissionRepository;
import com.easybus.repository.RolePermissionRepository;
import com.easybus.repository.RoleRepository;
import com.easybus.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

	private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

	@Autowired
	PermissionRepository permissionRepo;
	@Autowired
	RoleRepository roleRepo;
	@Autowired
	RolePermissionRepository rolePermissionRepository;

	@Override
	public Permission createPermission(Permission permission) {
		log.info(" Creating new permission: {}", permission.getPermissionName());
		return permissionRepo.save(permission);
	}

	@Override
	public List<Permission> createPermissions(List<Permission> permissions) {
		log.info(" Creating {} permissions in bulk", permissions.size());
		return permissionRepo.saveAll(permissions);
	}

	@Override
	public List<Permission> updatePermissions(List<Permission> permissions) {
		log.info(" Updating {} permissions in bulk", permissions.size());
		return permissionRepo.saveAll(permissions);
	}

	@Override
	public void deletePermission(Long id) {
		log.warn(" Deleting permission with id={}", id);
		permissionRepo.deleteById(id);
	}

	@Override
	public void deletePermissions(List<Long> ids) {
		log.warn(" Bulk delete for {} permissions", ids.size());
		permissionRepo.deleteAllById(ids);
	}

	@Override
	public List<Permission> getAllPermissions() {
		log.info(" Fetching all permissions");
		return permissionRepo.findAll();
	}

	@Override
	public Permission getPermissionById(Long id) {
		log.info(" Fetching permission with id={}", id);
		return permissionRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
	}

	@Override
	public void assignPermissionToRole(Long roleId, Long permissionId) {
		log.info(" Assigning permissionId={} to roleId={}", permissionId, roleId);

		Permission permission = permissionRepo.findById(permissionId)
				.orElseThrow(() -> new RuntimeException("Permission not found"));

		RolePermission rp = new RolePermission();
		rp.setRoleId(roleId);
	//	rp.setPermissionId(permissionId);
		rp.setPermissionName(permission.getId().toString());

		rolePermissionRepository.save(rp);
		log.debug(" Assigned permission '{}' to role {}", permission.getPermissionName(), roleId);
	}

	@Override
	public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
		log.info(" Assigning {} permissions to roleId={}", permissionIds.size(), roleId);

		
		 List<Permission> permissions = permissionRepo.findAllById(permissionIds);
		 if (permissions.size() != permissionIds.size()) {
	            throw new RuntimeException("Some permissions not found for IDs: " + permissionIds);
	        }
		 RolePermission rp = rolePermissionRepository.findByRoleId(roleId)
	                .orElse(new RolePermission());

		 String permissionString = permissionIds.stream()
                 .map(String::valueOf) .collect(Collectors.joining(",")); 
		 rp.setRoleId(roleId);
		    rp.setPermissionName(permissionString);
		    rp.setIsActive(true);
		    rp.setCreatedBy("admin");

		    rolePermissionRepository.save(rp);
	
	
	}

	@Override
	public void removePermissionFromRole(Long roleId, Long permissionId) {
		log.info(" Removing permissionId={} from roleId={}", permissionId, roleId);
	//	Role role = roleRepo.findById(roleId).orElseThrow();
		Role role = roleRepo.findById(roleId)
	            .orElseThrow(() -> new IllegalArgumentException("Role not found with id " + roleId));
//		Permission permission = permissionRepo.findById(permissionId).orElseThrow();
		 Permission permission = permissionRepo.findById(permissionId)
		            .orElseThrow(() -> new IllegalArgumentException("Permission not found with id " + permissionId));
		 List<String> currentPermissions = new ArrayList<>();
		    if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
		        currentPermissions = new ArrayList<>(Arrays.asList(role.getPermissions().split(",")));
		    }
		    currentPermissions.remove(permission.getPermissionName());
		//role.getPermissions().remove(permission);
		roleRepo.save(role);
		log.debug(" Removed permissionId={} from roleId={}", permissionId, roleId);
	}

	@Override
	public void removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
		log.info(" Removing {} permissions from roleId={}", permissionIds.size(), roleId);
		Role role = roleRepo.findById(roleId)
	            .orElseThrow(() -> new IllegalArgumentException("Role not found with id " + roleId));
		List<Permission> permissions = permissionRepo.findAllById(permissionIds);
		List<String> currentPermissions = new ArrayList<>();
	    if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
	        currentPermissions = new ArrayList<>(Arrays.asList(role.getPermissions().split(",")));
	    }

	    // Remove each permission UUID
	    for (Permission permission : permissions) {
	        currentPermissions.remove(permission.getPermissionName());
	    }

	    // Save back as CSV
	    role.setPermissions(String.join(",", currentPermissions));
		
		//role.getPermissions().removeAll(permissions);
		roleRepo.save(role);
		log.debug(" Removed {} permissions from roleId={}", permissions.size(), roleId);
	}

	@Override
	@Transactional
	public Permission updatePermission(Long id, Permission permission) {
		log.info(" Updating permission with id={}", id);

		Permission existing = permissionRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));

		existing.setPermissionName(permission.getPermissionName());
		existing.setUpdatedBy(permission.getUpdatedBy());
		existing.setIsActive(permission.getIsActive());
		existing.setVersion(existing.getVersion() + 1); // increment version

		Permission updated = permissionRepo.save(existing);
		log.debug(" Updated permission: {}", updated);
		return updated;
	}

}
