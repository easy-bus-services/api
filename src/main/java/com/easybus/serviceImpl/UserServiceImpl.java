package com.easybus.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.easybus.entity.User;
import com.easybus.model.PagedResponse;
import com.easybus.model.UserSpecification;
import com.easybus.repository.UserRepository;
import com.easybus.service.UserService;
import com.easybus.specification.UserSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
	private final ModelMapper modelMapper = new ModelMapper();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Create new user
    @Override
    public User createUser(User user) {
 if (user.getReferralId() == null || user.getReferralId().isEmpty()) {
		        user.setReferralId(generateReferralCode(user.getFullName()));
		    }
      	user.setStatus(User.Status.ACTIVE);// default active
        log.info(" Creating new user with email={} phone={}", user.getEmail(), user.getPhone());
        User saved = userRepository.save(user);
        log.debug(" User created successfully with id={}", saved.getId());
        return saved;
    }

    // ✅ Update existing user
    @Override
    public User updateUser(Long id, User user) {
        log.info(" Updating user with id={}", id);

		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with id " + id));

		modelMapper.map(user, existingUser); // auto maps all fields
 log.debug(" User updated successfully id={} email={}", updated.getId(), updated.getEmail());
		return userRepository.save(existingUser);
                });
    }
 
    //  Soft delete user (mark inactive)
    @Override
    public void softDeleteUser(Long id) {
        log.info(" Soft deleting user with id={}", id);

      		Optional<User> userOpt = userRepository.findById(id);
		if (userOpt.isPresent()) {
			User user = userOpt.get();

			user.setStatus(User.Status.INACTIVE);
			userRepository.save(user);
			    log.debug(" User soft deleted id={}", id);
		} else {
			throw new RuntimeException("User not found with id " + id);
		}
		}
     	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}   


    //  Get users with filters (id, email, phone)
    @Override
    public List<User> getUsers(Long id, String email, String phone) {
        log.info(" Fetching users with filters id={} email={} phone={}", id, email, phone);

        Specification<User> spec = Specification.where(UserSpecification.isActive())
                .and(UserSpecification.hasId(id))
                .and(UserSpecification.hasEmail(email))
                .and(UserSpecification.hasPhone(phone));

        List<User> users = userRepository.findAll(spec);
        log.debug(" Found {} active users", users.size());
        return users;
    }

  	// --- Bulk ---
	@Override
	public List<User> createUsers(List<User> users) {
		List<String> emails = users.stream().map(User::getEmail).collect(Collectors.toList());
		List<User> existingEmails = userRepository.findByEmailIn(emails);

		if (!existingEmails.isEmpty()) {
			List<String> list = existingEmails.stream().map(User::getEmail).toList();

			throw new RuntimeException("These emails already exist: " + list);
		}
		
		LocalDateTime now = LocalDateTime.now();
		users.forEach(u -> {
			  if (u.getReferralId() == null || u.getReferralId().isEmpty()) {
		            u.setReferralId(generateReferralCode(u.getFullName()));
		        }
			u.setStatus(User.Status.ACTIVE);
			u.setCreatedDate(now);
			u.setCreatedBy("system"); // replace with logged-in user if available
		});

		return userRepository.saveAll(users);
	}

	@Override
	public List<User> updateUsers(List<User> updateRequests) {
		List<User> updatedUsers = new ArrayList<>();

		for (User req : updateRequests) {
			User user = userRepository.findById(req.getId())
					.orElseThrow(() -> new RuntimeException("User not found with id " + req.getId()));

			// update only non-null fields
			if (req.getFullName() != null)
				user.setFullName(req.getFullName());
			if (req.getEmail() != null)
				user.setEmail(req.getEmail());
			if (req.getPhoneNumber() != null)
				user.setPhoneNumber(req.getPhoneNumber());
			if (req.getPasswordHash() != null)
				user.setPasswordHash(req.getPasswordHash());
			if (req.getStatus() != null)
				user.setStatus(req.getStatus());
			if (req.getReferralId() != null)
				user.setReferralId(req.getReferralId());
			updatedUsers.add(user);
		}

		return userRepository.saveAll(updatedUsers);
	}

	@Override
	public void softDeleteUsers(List<Long> ids) {
		List<User> users = userRepository.findAllById(ids);

		if (users.isEmpty()) {
			throw new RuntimeException("No users found for given IDs");
		}

		users.forEach(user -> {

			user.setStatus(User.Status.INACTIVE); // mark as inactive
			user.setUpdatedDate(LocalDateTime.now());
			user.setUpdatedBy("system"); // or from logged-in user context
		});

		userRepository.saveAll(users); // bulk update
	}

	@Override
	public User getUser(Long id) { // 👈 Implementation
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
	}

	@Override
	public PagedResponse<User> searchUsers(String email, String name, String status, String phoneNumber, int page,
			int size, String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
		Specification<User> spec = Specification.where(null); // start empty

		spec = spec.and(UserSpecification.hasEmail(email)).and(UserSpecification.hasName(name))
				.and(UserSpecification.hasStatus(status)).and(UserSpecification.hasPhoneNumber(phoneNumber));

		Page<User> userPage = userRepository.findAll(spec, pageable);

		return new PagedResponse<>(userPage.getContent(), userPage.getTotalPages(), userPage.getTotalElements(),
				userPage.getNumberOfElements());
	}
	

	    
//	    private String generateReferralCode(String fullName) {
//	        if (fullName == null || fullName.trim().isEmpty()) {
//	            fullName = "USR"; // fallback if name is missing
//	        }
//
//	        String prefix = fullName.replaceAll("\\s+", "")
//	                                .toUpperCase()
//	                                .substring(0, Math.min(3, fullName.length()));
//
//	        String code = null;
//
//	        // try max 5 times to avoid duplicates
//	        for (int attempt = 0; attempt < 5; attempt++) {
//	            String timeSuffix = String.valueOf(System.currentTimeMillis());
//	            timeSuffix = timeSuffix.substring(timeSuffix.length() - 3);
//
//	            code = prefix + timeSuffix + (attempt > 0 ? attempt : "");
//
//	            if (!userRepository.existsByReferralId(code)) {
//	                break; // found unique one
//	            }
//	        }
//
//	        return code;
//	    }


	    
	    private String generateReferralCode(String fullName) {
	        String prefix = (fullName != null && fullName.length() >= 3)
	                ? fullName.substring(0, 3).toUpperCase()
	                : "USR";

	        String referralId;
	        do {
	            int random = (int)(Math.random() * 900) + 100; // 3-digit random number
	            referralId = prefix + random;
	        } while (userRepository.existsByReferralId(referralId)); // check uniqueness in DB

	        return referralId;
	    }
	
}
}
