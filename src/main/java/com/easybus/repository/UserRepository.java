// UserRepository.java
package com.easybus.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.easybus.entity.User;

@Repository

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	List<User> findByEmailIn(List<String> emails);
	  Optional<User> findByReferralId(String referralId);
	  boolean existsByReferralId(String referralId);
}
