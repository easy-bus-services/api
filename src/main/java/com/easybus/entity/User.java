// User.java
package com.easybus.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // ✅ change if your table name is different
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;
	@NotBlank(message = "Full name is required")
	@Column(name = "full_name", length = 100, nullable = false)
	private String fullName;
	@NotBlank(message = "Email is required")
	 @Email(message = "Invalid email format")
	@Column(name = "email", length = 255, unique = true, nullable = false)
	private String email;
	@Size(min = 10, max = 20, message = "Phone number must be between 10-20 characters")
	@Column(name = "phone_number", length = 20, unique = true)
	private String phoneNumber;

	@NotBlank(message = "Password is required")
	@Column(name = "password_hash", length = 255, nullable = false)
	private String passwordHash;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;
	@UpdateTimestamp
	@Column(name = "update_date")
	private LocalDateTime updatedDate;
	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;
	@LastModifiedBy
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "referral_id", unique = true, nullable = true)
    private String referralId;   // Random unique referral code

    // Who referred me
    @ManyToOne
    @JoinColumn(name = "referred_by_id")
    private User referredBy;

    // Who I referred
    @OneToMany(mappedBy = "referredBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> referredUsers = new HashSet<>();

	public enum Status {
		ACTIVE, INACTIVE, BLOCKED
	}

}
