package com.easybus.service;

import com.easybus.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    User updateUser(Long id, User user);

    void softDeleteUser(Long id);

    List<User> getUsers(Long id, String email, String phone);


	List<User> searchUsers(Long id, String email, String phone);
}
