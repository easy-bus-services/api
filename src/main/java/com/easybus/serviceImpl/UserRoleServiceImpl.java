package com.easybus.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.easybus.entity.Role;
import com.easybus.entity.User;
import com.easybus.entity.UserRole;
import com.easybus.repository.RoleRepository;
import com.easybus.repository.UserRepository;
import com.easybus.repository.UserRoleRepository;
import com.easybus.service.UserRoleService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserRoleRepository userRoleRepository;

	public UserRoleServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			UserRoleRepository userRoleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	// Assign role to user
	public UserRole assignRoleToUser(Long userId, Long roleId) {
		log.info("Assigning roleId={} to userId={}", roleId, userId);

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		Role role = roleRepository.findById(roleId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

		// Check if role already assigned and not soft-deleted
		boolean exists = userRoleRepository.findByUserIdAndDeletedAtIsNull(userId).stream()
				.anyMatch(ur -> ur.getRole().getRoleId().equals(roleId));
		if (exists) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role already assigned to user");
		}

		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		userRole.setAssignedAt(LocalDateTime.now());
		UserRole saved = userRoleRepository.save(userRole);

		log.info("Role assigned successfully, userRoleId={}", saved.getUserRoleId());
		return saved;
	}

	@Override
	// Soft delete role from user
	public void removeRoleFromUser(Long userRoleId) {
		log.info("Soft deleting userRoleId={}", userRoleId);
		UserRole userRole = userRoleRepository.findById(userRoleId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserRole not found"));
		userRole.setDeletedAt(LocalDateTime.now());
		userRoleRepository.save(userRole);
		log.info("UserRole soft deleted, userRoleId={}", userRoleId);
	}

	@Override
	// Get roles by user
	public List<Role> getRolesByUser(Long userId) {
		return userRoleRepository.findByUserIdAndDeletedAtIsNull(userId).stream().map(UserRole::getRole)
				.collect(Collectors.toList());
	}
}