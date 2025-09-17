package com.easybus.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easybus.entity.Permission;
import com.easybus.entity.Role;
import com.easybus.repository.PermissionRepository;
import com.easybus.repository.RolePermissionRepository;
import com.easybus.repository.RoleRepository;
import com.easybus.service.PermissionService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
	private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);
	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private RolePermissionRepository rolePermissionRepository;

	@Autowired
	RoleRepository roleRepository;
	// -------------------- CRUD --------------------

	@Override
	public Permission createPermission(Permission permission) {
		log.info(" Creating new permission: {}", permission.getPermissionName());
		if (permissionRepository.existsByPermissionName(permission.getPermissionName())) {
			throw new RuntimeException("Permission already exists with name: " + permission.getPermissionName());
		}
		return permissionRepository.save(permission);
	}

	@Override
	public List<Permission> createPermissions(List<Permission> permissions) {
		log.info(" Creating {} permissions in bulk", permissions.size());
		return permissionRepository.saveAll(permissions);
	}

	@Override
	public Permission updatePermission(Long id, Permission permission) {
		log.info(" Updating {} permissions in bulk", id);
		Permission existing = permissionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Permission not found with ID: " + id));

		existing.setPermissionName(permission.getPermissionName());
		existing.setIsActive(permission.getIsActive());
		existing.setUpdatedBy(permission.getUpdatedBy());
		existing.setVersion(existing.getVersion() + 1);
		return permissionRepository.save(existing);
	}

	@Override
	public List<Permission> updatePermissions(List<Permission> permissions) {
		log.info(" Updating {} permissions in bulk", permissions.size());
		return permissions.stream().map(p -> updatePermission(p.getId(), p)).collect(Collectors.toList());
	}

	@Override
	public void deletePermission(Long id) {
		log.warn(" Deleting permission with id={}", id);
		permissionRepository.deleteById(id);
	}

	@Override
	public void deletePermissions(List<Long> ids) {
		log.warn(" Bulk delete for {} permissions", ids.size());
		permissionRepository.deleteAllById(ids);
	}

	@Override
	public List<Permission> getAllPermissions() {
		log.info(" Fetching all permissions");
		return permissionRepository.findAll();
	}

	@Override
	public Permission getPermissionById(Long id) {
		log.info(" Fetching permission with id={}", id);
		return permissionRepository.findById(id).orElse(null);
	}

	// -------------------- ASSIGN / REMOVE --------------------

//	@Override
//	@Transactional
//	public void assignPermissionToRole(Long roleId, List<Long> permissionIds) {
//	    log.info("Assigning permissionIds={} to roleId={}", permissionIds, roleId);
//
//	 // Fetch role from DB
//	    Role role = roleRepository.findById(roleId)
//	            .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
//
//	    // Fetch all permissions from DB
//	    List<Permission> permissions = permissionRepository.findAllById(permissionIds);
//	    if (permissions.size() != permissionIds.size()) {
//	        throw new RuntimeException("Some permissions not found for IDs: " + permissionIds);
//	    }
//
//	    // Assign each permission
//	    for (Permission permission : permissions) {
//	        boolean exists = rolePermissionRepository.existsByRoleAndPermission(role, permission);
//	        if (!exists) {
//	            RolePermission rp = new RolePermission();
//	            rp.setRole(role);               // must be managed entity
//	            rp.setPermission(permission);   // must be managed entity
//	            rolePermissionRepository.save(rp);
//	            log.debug("Assigned permissionId={} to roleId={}", permission.getId(), roleId);
//	        } else {
//	            log.warn("PermissionId={} already assigned to roleId={}", permission.getId(), roleId);
//	        }
//	    }
//	}
//
//
////	@Override
////	@Transactional
////	public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
////	    for (Long permissionId : permissionIds) {
////	        assignPermissionToRole(roleId, permissionId);
////	    }
////	}
//
//	@Override
//	@Transactional
//	public void removePermissionFromRole(Long roleId, Long permissionId) {
//	    log.info("Removing permissionId={} from roleId={}", permissionId, roleId);
//
//	    // Fetch role
//	    Role role = roleRepository.findById(roleId)
//	            .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
//
//	    // Fetch permission
//	    Permission permission = permissionRepository.findById(permissionId)
//	            .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + permissionId));
//
//	    // Find RolePermission mapping
//	    RolePermission rolePermission = rolePermissionRepository.findByRoleAndPermission(role, permission)
//	            .orElseThrow(() -> new RuntimeException(
//	                    "PermissionId " + permissionId + " is not assigned to roleId " + roleId));
//
//	    // Delete mapping
//	    rolePermissionRepository.delete(rolePermission);
//
//	    log.debug("Removed permissionId={} from roleId={}", permissionId, roleId);
//	
//	}
//
//	@Override
//	@Transactional
//	public void removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
//	    for (Long permissionId : permissionIds) {
//	        removePermissionFromRole(roleId, permissionId);
//	    }
//	}

	
	   // ---------------- Bulk Assign from List ----------------
//    @Override
//    @Transactional
//    public void assignPermissionToRole(Long roleId, List<Long> permissionIds) {
//        log.info("Assigning permissions={} to roleId={}", permissionIds, roleId);
//
//        Role role = roleRepository.findById(roleId)
//                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
//
//        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
//        if (permissions.size() != permissionIds.size()) {
//            throw new RuntimeException("Some permissions not found for IDs: " + permissionIds);
//        }
//
//        for (Permission permission : permissions) {
//            boolean exists = rolePermissionRepository.existsByRoleAndPermission(role, permission);
//            if (!exists) {
//                RolePermission rp = new RolePermission();
//                rp.setRole(role);
//                rp.setPermission(permission);
//                rolePermissionRepository.save(rp);
//                log.debug("Assigned permissionId={} to roleId={}", permission.getId(), roleId);
//            } else {
//                log.warn("PermissionId={} already assigned to roleId={}", permission.getId(), roleId);
//            }
//        }
//    }
//
//    // ---------------- Assign from CSV ----------------
//    @Override
//    @Transactional
//    public void assignPermissionsFromCsv(Long roleId, String permissionIdsCsv) {
//        if (permissionIdsCsv == null || permissionIdsCsv.isBlank()) {
//            throw new RuntimeException("No permissions provided in CSV");
//        }
//
//        List<Long> permissionIds = Arrays.stream(permissionIdsCsv.split(","))
//                                         .map(String::trim)
//                                         .map(Long::parseLong)
//                                         .toList();
//
//        assignPermissionToRole(roleId, permissionIds);
//    }
//
//    // ---------------- Remove Single Permission ----------------
//    @Override
//    @Transactional
//    public void removePermissionFromRole(Long roleId, Long permissionId) {
//        log.info("Removing permissionId={} from roleId={}", permissionId, roleId);
//
//        RolePermission rp = rolePermissionRepository.findByRole_IdAndPermission_Id(roleId, permissionId)
//                .orElseThrow(() -> new RuntimeException(
//                        "PermissionId " + permissionId + " is not assigned to roleId " + roleId));
//
//        rolePermissionRepository.delete(rp);
//        log.debug("Removed permissionId={} from roleId={}", permissionId, roleId);
//    }
//
//    // ---------------- Bulk Remove ----------------
//    @Override
//    @Transactional
//    public void removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
//        log.info("Removing permissions={} from roleId={}", permissionIds, roleId);
//
//        for (Long permissionId : permissionIds) {
//            removePermissionFromRole(roleId, permissionId);
//        }
//    }
//
//    // ---------------- Remove from CSV ----------------
//    @Override
//    @Transactional
//    public void removePermissionsFromCsv(Long roleId, String permissionIdsCsv) {
//        if (permissionIdsCsv == null || permissionIdsCsv.isBlank()) {
//            throw new RuntimeException("No permissions provided in CSV");
//        }
//
//        List<Long> permissionIds = Arrays.stream(permissionIdsCsv.split(","))
//                                         .map(String::trim)
//                                         .map(Long::parseLong)
//                                         .toList();
//
//        removePermissionsFromRole(roleId, permissionIds);
//    }
//    
//    
//    
    
	  // Assign multiple permissions to a role
    @Transactional
    @Override
    public List<Long> assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
//        Role role = roleRepository.findById(roleId)
//                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        Role role = roleRepository.findActiveRoleWithPermissions(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found or deleted: " + roleId));
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
          throw new RuntimeException("Some permissions not found for IDs: " + permissionIds);
      }
        List<Long> assignedIds = new ArrayList<>();

        for (Permission permission : permissions) {
            if (!role.getPermissions().contains(permission)) {
                role.getPermissions().add(permission);
                assignedIds.add(permission.getId());
            }
        }

        roleRepository.save(role);
        return assignedIds;
    }

    // Remove multiple permissions from a role
    @Override
    @Transactional
    public List<Long> removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
//        Role role = roleRepository.findById(roleId)
//                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        Role role = roleRepository.findActiveRoleWithPermissions(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found or deleted: " + roleId));

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);

        List<Long> removedIds = new ArrayList<>();

        for (Permission permission : permissions) {
            if (role.getPermissions().contains(permission)) {
                role.getPermissions().remove(permission);
                removedIds.add(permission.getId());
            }
        }

        roleRepository.save(role);
        return removedIds;
    }

    // Get all permissions of a role
    @Override
    public Set<Permission> getPermissionsByRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
        return role.getPermissions();
    }

}



