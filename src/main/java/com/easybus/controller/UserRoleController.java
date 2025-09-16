package com.easybus.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easybus.entity.Role;
import com.easybus.entity.UserRole;
import com.easybus.service.UserRoleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user-roles")
@Slf4j
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    // Assign role
    @PostMapping("/assign")
    public ResponseEntity<UserRole> assignRole(@RequestParam Long userId,
                                               @RequestParam Long roleId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRoleService.assignRoleToUser(userId, roleId));
    }

    // Remove role (soft delete)
    @DeleteMapping("/{userRoleId}")
    public ResponseEntity<String> removeRole(@PathVariable Long userRoleId) {
        userRoleService.removeRoleFromUser(userRoleId);
        return ResponseEntity.ok("Role removed from user (soft delete)");
    }

    // Get roles for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Role>> getRolesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userRoleService.getRolesByUser(userId));
    }
}

