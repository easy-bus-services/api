package com.easybus.model;

import org.springframework.data.jpa.domain.Specification;

import com.easybus.entity.User;
import com.easybus.entity.User.Status;

public class UserSpecification {

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isEmpty()) return null;
            return cb.equal(cb.lower(root.get("email")), email.toLowerCase());
        };
    }

    public static Specification<User> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) return null;
            return cb.like(cb.lower(root.get("fullName")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<User> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty()) return null;
            // Convert string to enum safely
            Status enumStatus;
            try {
                enumStatus = Status.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null; // ignore invalid status
            }
            return cb.equal(root.get("status"), enumStatus);
        };
    }

    public static Specification<User> hasPhoneNumber(String phoneNumber) {
        return (root, query, cb) -> {
            if (phoneNumber == null || phoneNumber.isEmpty()) return null;
            return cb.equal(root.get("phoneNumber"), phoneNumber);
        };
    }
}
