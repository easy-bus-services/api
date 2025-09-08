package com.easybus.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easybus.Constants;
import com.easybus.entity.User;
import com.easybus.model.ResponseMessage;
import com.easybus.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Single User Create
    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody User user) {
        log.info(" Create user request: {}", user);

        if (user == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "Invalid user request, body cannot be null")
            );
        }

        if (user.getIsActive() == null) {
            user.setIsActive(true); // default true
        }

        User created = userService.createUser(user);
        log.info(" User created successfully: {}", created);

        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "User created successfully", created)
        );
    }

    // ✅ Bulk Users Create
    @PostMapping("/create-bulk")
    public ResponseEntity<ResponseMessage> createBulkUsers(@RequestBody List<User> users) {
        log.info(" Bulk create users request: count={}", users.size());

        if (users == null || users.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "User list cannot be empty")
            );
        }

        users.forEach(user -> {
            if (user.getIsActive() == null) {
                user.setIsActive(true);
            }
        });

        List<User> createdList = users.stream()
                .map(userService::createUser)
                .collect(Collectors.toList());

        log.info(" Bulk users created: count={}", createdList.size());

        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Users created successfully", createdList)
        );
    }

    // ✅ Update User
    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info(" Update request for User ID {}: {}", id, user);

        if (user == null) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage(400, Constants.FAILURE, "User request body cannot be null")
            );
        }

        try {
            User updated = userService.updateUser(id, user);
            log.info(" User updated successfully: {}", updated);

            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "User updated successfully", updated)
            );
        } catch (RuntimeException e) {
            log.error(" Failed to update user: {}", e.getMessage(), e);
            return ResponseEntity.status(404).body(
                    new ResponseMessage(404, Constants.FAILED, "User not found with ID " + id)
            );
        }
    }

    // ✅ Soft Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> softDeleteUser(@PathVariable Long id) {
        log.warn(" Soft delete request for User ID {}", id);

        try {
            userService.softDeleteUser(id);
            log.info(" User ID {} marked as deleted", id);

            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "User deleted successfully")
            );
        } catch (Exception e) {
            log.error(" Failed to delete user ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(404).body(
                    new ResponseMessage(404, Constants.FAILED, "User not found with ID " + id)
            );
        }
    }

    // ✅ Get Users (with optional filters)
    @GetMapping
    public ResponseEntity<ResponseMessage> getUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone
    ) {
        log.info(" Get users request: id={}, email={}, phone={}", id, email, phone);

        List<User> users = userService.getUsers(id, email, phone);

        if (users == null || users.isEmpty()) {
            log.warn(" No users found with given filters");
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "No users found with given filters")
            );
        }

        log.info(" Retrieved {} users", users.size());
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Users retrieved successfully", users)
        );
    }

    // ✅ Search Users
    @GetMapping("/search")
    public ResponseEntity<ResponseMessage> searchUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone
    ) {
        log.info(" Search users request: id={}, email={}, phone={}", id, email, phone);

        List<User> users = userService.searchUsers(id, email, phone);

        if (users.isEmpty()) {
            log.warn(" No users found for given search criteria");
            return ResponseEntity.ok(
                    new ResponseMessage(200, Constants.SUCCESS, "No users found for given search criteria")
            );
        }

        log.info(" Found {} users for given criteria", users.size());
        return ResponseEntity.ok(
                new ResponseMessage(200, Constants.SUCCESS, "Users retrieved successfully", users)
        );
    }
}
