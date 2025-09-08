package com.easybus.specification;

import com.easybus.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> email == null ? null : cb.equal(root.get("email"), email);
    }

    public static Specification<User> hasPhone(String phone) {
        return (root, query, cb) -> phone == null ? null : cb.equal(root.get("phone"), phone);
    }

    public static Specification<User> hasId(Long id) {
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<User> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }
}
