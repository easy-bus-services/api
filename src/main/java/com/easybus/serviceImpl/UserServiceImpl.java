package com.easybus.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.easybus.entity.User;
import com.easybus.repository.UserRepository;
import com.easybus.service.UserService;
import com.easybus.specification.UserSpecification;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Create new user
    @Override
    public User createUser(User user) {
        user.setIsActive(true); // default active
        log.info("👤 Creating new user with email={} phone={}", user.getEmail(), user.getPhone());
        User saved = userRepository.save(user);
        log.debug("✅ User created successfully with id={}", saved.getId());
        return saved;
    }

    // ✅ Update existing user
    @Override
    public User updateUser(Long id, User user) {
        log.info("✏️ Updating user with id={}", id);

        return userRepository.findById(id)
                .map(existing -> {
                    existing.setName(user.getName());
                    existing.setEmail(user.getEmail());
                    existing.setPhone(user.getPhone());
                    existing.setUpdateDate(java.time.LocalDateTime.now());
                    User updated = userRepository.save(existing);
                    log.debug("✅ User updated successfully id={} email={}", updated.getId(), updated.getEmail());
                    return updated;
                })
                .orElseThrow(() -> {
                    log.error("❌ User not found with id={}", id);
                    return new RuntimeException("User not found");
                });
    }

    // ✅ Soft delete user (mark inactive)
    @Override
    public void softDeleteUser(Long id) {
        log.info("🗑️ Soft deleting user with id={}", id);

        userRepository.findById(id).ifPresent(user -> {
            user.setIsActive(false);
            user.setUpdateDate(java.time.LocalDateTime.now());
            userRepository.save(user);
            log.debug("✅ User soft deleted id={}", id);
        });
    }

    // ✅ Get users with filters (id, email, phone)
    @Override
    public List<User> getUsers(Long id, String email, String phone) {
        log.info("📋 Fetching users with filters id={} email={} phone={}", id, email, phone);

        Specification<User> spec = Specification.where(UserSpecification.isActive())
                .and(UserSpecification.hasId(id))
                .and(UserSpecification.hasEmail(email))
                .and(UserSpecification.hasPhone(phone));

        List<User> users = userRepository.findAll(spec);
        log.debug("✅ Found {} active users", users.size());
        return users;
    }

    // ✅ Search users (similar to get but flexible)
    @Override
    public List<User> searchUsers(Long id, String email, String phone) {
        log.info("🔎 Searching users with criteria id={} email={} phone={}", id, email, phone);

        List<User> users = userRepository.findAll(
                Specification.where(UserSpecification.hasId(id))
                        .and(UserSpecification.hasEmail(email))
                        .and(UserSpecification.hasPhone(phone))
                        .and(UserSpecification.isActive())
        );

        log.debug("✅ Found {} matching users", users.size());
        return users;
    }
}
