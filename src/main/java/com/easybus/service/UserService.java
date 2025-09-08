package com.easybus.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.easybus.entity.User;
import com.easybus.model.PagedResponse;

public interface UserService {

	// Single operations
	User createUser(User user);

	User updateUser(Long id, User user);

	void softDeleteUser(Long id);

	List<User> getAllUsers();

	// Bulk operations
	List<User> createUsers(List<User> users);

	List<User> updateUsers(List<User> users);

	void softDeleteUsers(List<Long> ids);

	// List<User> searchUsers();
	User getUser(Long id);

	PagedResponse<User> searchUsers(String email, String name, String status, String phonenumber, int page, int size,
			String sortBy);


}
