package com.easybus.serviceImpl;

import java.util.List;

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

    @Autowired PermissionRepository permissionRepo;
    @Autowired RoleRepository roleRepo;
    @Autowired RolePermissionRepository rolePermissionRepository;

    @Override
    public Permission createPermission(Permission permission) {
        log.info(" Creating new permission: {}", permission.getPermissionName());
        return permissionRepo.save(permission);
    }

    @Override
    public List<Permission> createPermissions(List<Permission> permissions) {
        log.info(" Creating {} permissions in bulk", permissions.size());
        for (Permission permission : permissions) {
        	  Long parentId = permission.getParentId();
            if (parentId != null &&   parentId > 0) {
                Permission parent = permissionRepo.findById(parentId)
                        .orElseThrow(() -> new RuntimeException("Parent not found for ID: " + parentId));
                permission.setParent(parent);
            } else {
                permission.setParent(null);
            }
        }
        return permissionRepo.saveAll(permissions);
    }

    @Override
    public List<Permission> updatePermissions(List<Permission> permissions) {
        log.info(" Updating {} permissions in bulk", permissions.size());
        for (Permission permission : permissions) {
            Long parentId = permission.getParentId();

            //  Only set parent if parentId is provided and > 0
            if (parentId != null && parentId > 0) {
                Permission parent = permissionRepo.findById(parentId)
                        .orElseThrow(() -> new RuntimeException("Parent not found for ID: " + parentId));
                permission.setParent(parent);
            } else if (parentId != null && parentId == 0) {
                // explicitly remove parent if parentId = 0
                permission.setParent(null);
            }
        }
        return permissionRepo.saveAll(permissions);
    }

    @Override
    public void deletePermission(Long id) {
        log.warn(" Deleting permission with id={}", id);
        Permission permission = permissionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with ID: " + id));

        // Optional: detach children before deletion to maintain DB consistency
        permission.getChildren().forEach(child -> child.setParent(null));
        permissionRepo.deleteById(id);
    }

    @Override
    public void deletePermissions(List<Long> ids) {
        log.warn(" Bulk delete for {} permissions", ids.size());
        List<Permission> permissions = permissionRepo.findAllById(ids);

        // Optional: detach children before deletion
        for (Permission permission : permissions) {
            permission.getChildren().forEach(child -> child.setParent(null));
        }
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
        rp.setPermissionId(permissionId);
        rp.setPermissionName(permission.getPermissionName());

        rolePermissionRepository.save(rp);
        log.debug(" Assigned permission '{}' to role {}", permission.getPermissionName(), roleId);
    }

    @Override
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        log.info(" Assigning {} permissions to roleId={}", permissionIds.size(), roleId);
        for (Long pid : permissionIds) {
            assignPermissionToRole(roleId, pid);
        }
    }

    @Override
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        log.info(" Removing permissionId={} from roleId={}", permissionId, roleId);
        Role role = roleRepo.findById(roleId).orElseThrow();
        Permission permission = permissionRepo.findById(permissionId).orElseThrow();
        role.getPermissions().remove(permission);
        roleRepo.save(role);
        log.debug(" Removed permissionId={} from roleId={}", permissionId, roleId);
    }

    @Override
    public void removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
        log.info(" Removing {} permissions from roleId={}", permissionIds.size(), roleId);
        Role role = roleRepo.findById(roleId).orElseThrow();
        List<Permission> permissions = permissionRepo.findAllById(permissionIds);
        role.getPermissions().removeAll(permissions);
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
    
    
    // Get all children for a permission
    public List<Permission> getChildren(Long parentId) {
        Permission parent = permissionRepo.findById(parentId)
                                      .orElseThrow(() -> new RuntimeException("Parent not found"));
        return permissionRepo.findByParent(parent);
    }
}
