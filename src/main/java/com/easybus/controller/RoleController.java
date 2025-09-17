package com.easybus.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/roles")

public class RoleController {

	private static final Logger log = LoggerFactory.getLogger(RoleController.class);

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@PostMapping(value = "/createrole")
	public ResponseEntity<Role> createRole(@RequestParam String roleName,
			@RequestParam(required = false) String createdBy) {
		log.info("API: Create role request for userId={} roleName='{}' permissions={}",  roleName);
		Role role = roleService.createRole(roleName, createdBy);
		return ResponseEntity.status(HttpStatus.CREATED).body(role);
	}

	@GetMapping(value = "/getroles")
	public ResponseEntity<List<Role>> getAllRoles() {
		log.info("API: Get all roles");
		List<Role> roles = roleService.getAllRoles();
		return ResponseEntity.ok(roles);
	}

	@GetMapping("/getroleId/{roleId}")
	public ResponseEntity<Role> getRoleById(@PathVariable Long roleId) {
		log.info("API: Get role by roleId={}", roleId);
		Role role = roleService.getRoleById(roleId);
		return ResponseEntity.ok(role);
	}

	@PutMapping("/update/{roleId}")
	public ResponseEntity<Role> updateRole(@PathVariable Long roleId, @RequestParam String roleName,
			@RequestParam(required = false) String updatedBy) {
		log.info("API: Update role roleId={} roleName='{}' ", roleId, roleName);
		
		Role updated = roleService.updateRole(roleId, roleName, updatedBy);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{roleId}")
	public ResponseEntity<Map<String, Object>> deleteRole(@PathVariable Long roleId,
			@RequestParam(required = false) String deletedBy) {
		log.info("API: Delete role with roleId={}", roleId);
		roleService.deleteRole(roleId, deletedBy);
		return ResponseEntity.ok(Map.of("message", "Role soft deleted successfully", "status", HttpStatus.OK.value(),
				"timestamp", LocalDateTime.now()));
	}
}
