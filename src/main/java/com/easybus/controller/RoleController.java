package com.easybus.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easybus.entity.Role;
import com.easybus.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

	private static final Logger log = LoggerFactory.getLogger(RoleController.class);

	private final RoleService roleService;

	@PostMapping("/{userId}")
	public ResponseEntity<Role> createRole(@PathVariable Long userId, @RequestParam String roleName,
			@RequestParam List<String> permissions) {

		log.info("API: Create role request for userId={} roleName='{}' permissions={}", userId, roleName, permissions);
		Set<String> permissionSet = new HashSet<>(permissions);
		Role role = roleService.createRole(userId, roleName, permissionSet);
		return ResponseEntity.status(201).body(role);
	}

	@GetMapping("/{roleId}")
	public ResponseEntity<Role> getRoleById(@PathVariable Long roleId) {
		log.info("API: Get role by roleId={}", roleId);
		Role role = roleService.getRoleById(roleId);
		return ResponseEntity.ok(role);
	}

	@GetMapping
	public ResponseEntity<List<Role>> getAllRoles() {
		log.info("API: Get all roles");
		List<Role> roles = roleService.getAllRoles();
		return ResponseEntity.ok(roles);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Role>> getRolesByUser(@PathVariable Long userId) {
		log.info("API: Get roles for userId={}", userId);
		List<Role> roles = roleService.getRolesByUser(userId);
		return ResponseEntity.ok(roles);
	}

	@PutMapping("/{roleId}")
	public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestParam String roleName,
			@RequestParam List<String> permissions) {

		log.info("API: Update role roleId={} roleName='{}' permissions={}", roleId, roleName, permissions);
		Set<String> permissionSet = new HashSet<>(permissions);
		Role role = roleService.updateRole(roleId, roleName, permissionSet);
		return ResponseEntity.ok(role);
	}

	@DeleteMapping("/{roleId}")
	public ResponseEntity<String> deleteRole(@PathVariable Long roleId) {
		log.info("API: Delete role with roleId={}", roleId);
		roleService.deleteRole(roleId);
		return ResponseEntity.ok("Role deleted successfully");
	}
}
