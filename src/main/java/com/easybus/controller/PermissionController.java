package com.easybus.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybus.Constants;
import com.easybus.entity.Permission;
import com.easybus.model.ResponseMessage;
import com.easybus.service.PermissionService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/permissions")
public class PermissionController {

    private static final Logger log = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionService permissionService;

    // -------------------- CREATE --------------------

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> create(@RequestBody Permission permission) {
        log.info(" Create Permission request: {}", permission);
        if (permission == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Permission data cannot be null"));
        }
        try {
            Permission saved = permissionService.createPermission(permission);
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "Permission created successfully", saved));
        } catch (Exception e) {
            log.error(" Error creating permission: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                    new ResponseMessage(500, Constants.FAILED, "Error creating permission: " + e.getMessage()));
        }
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<ResponseMessage> createBulk(@RequestBody List<Permission> permissions) {
        log.info(" Bulk Create Permissions request: {}", permissions);
        if (permissions == null || permissions.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Permissions list cannot be empty"));
        }
        List<Permission> savedList = permissionService.createPermissions(permissions);
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permissions created successfully", savedList));
    }

    // -------------------- UPDATE --------------------

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updatePermission(@PathVariable Long id,
                                                            @RequestBody Permission permission) {
        log.info(" Update Permission request for ID {}: {}", id, permission);
        if (id == null || permission == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Invalid input data"));
        }
        Permission updated = permissionService.updatePermission(id, permission);
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permission updated successfully", updated));
    }

    @PutMapping("/update/bulk")
    public ResponseEntity<ResponseMessage> updateBulk(@RequestBody List<Permission> permissions) {
        log.info(" Bulk Update Permissions request: {}", permissions);
        if (permissions == null || permissions.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Permission list cannot be empty"));
        }
        List<Permission> updatedList = permissionService.updatePermissions(permissions);
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permissions updated successfully", updatedList));
    }

    // -------------------- DELETE --------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable Long id) {
        log.warn(" Delete Permission request for ID {}", id);
        if (id == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Permission ID cannot be null"));
        }
        permissionService.deletePermission(id);
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permission deleted successfully"));
    }

    @DeleteMapping("/delete/bulk")
    public ResponseEntity<ResponseMessage> deleteBulk(@RequestBody List<Long> ids) {
        log.warn(" Bulk Delete request for Permission IDs {}", ids);
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "IDs list cannot be empty"));
        }
        permissionService.deletePermissions(ids);
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permissions deleted successfully"));
    }

    // -------------------- GET --------------------

    @GetMapping("/getAlllist")
    public ResponseEntity<ResponseMessage> getAll() {
        log.info(" Fetch all permissions request received");
        List<Permission> list = permissionService.getAllPermissions();
        if (list.isEmpty()) {
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "No permissions found", list));
        }
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permissions retrieved successfully", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getById(@PathVariable Long id) {
        log.info(" Fetch Permission by ID {}", id);
        if (id == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Permission ID cannot be null"));
        }
        Permission permission = permissionService.getPermissionById(id);
        if (permission == null) {
            return ResponseEntity.ok(
                    new ResponseMessage(404, Constants.FAILURE, "Permission not found with ID: " + id));
        }
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permission retrieved successfully", permission));
    }

    // -------------------- ASSIGN / REMOVE --------------------

    @PostMapping("/assign/{roleId}/{permissionId}")
    public ResponseEntity<ResponseMessage> assignPermission(@PathVariable Long roleId,
                                                            @PathVariable Long permissionId) {
        log.info(" Assign Permission ID {} to Role ID {}", permissionId, roleId);
        permissionService.assignPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permission assigned successfully"));
    }

    @PostMapping("/assign/bulk/{roleId}")
    public ResponseEntity<ResponseMessage> assignPermissions(@PathVariable Long roleId,
                                                             @RequestBody List<Long> permissionIds) {
        log.info(" Bulk Assign Permissions {} to Role ID {}", permissionIds, roleId);
        if (permissionIds == null || permissionIds.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Permission IDs list cannot be empty"));
        }
        permissionService.assignPermissionsToRole(roleId, permissionIds);
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permissions assigned successfully"));
    }

    @DeleteMapping("/remove/{roleId}/{permissionId}")
    public ResponseEntity<ResponseMessage> removePermission(@PathVariable Long roleId,
                                                            @PathVariable Long permissionId) {
        log.warn(" Remove Permission ID {} from Role ID {}", permissionId, roleId);
        permissionService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permission removed successfully"));
    }

    @DeleteMapping("/remove/bulk/{roleId}")
    public ResponseEntity<ResponseMessage> removePermissions(@PathVariable Long roleId,
                                                             @RequestBody List<Long> permissionIds) {
        log.warn("Bulk Remove Permissions {} from Role ID {}", permissionIds, roleId);
        if (permissionIds == null || permissionIds.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Permission IDs list cannot be empty"));
        }
        permissionService.removePermissionsFromRole(roleId, permissionIds);
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Permissions removed successfully"));
    }
}
