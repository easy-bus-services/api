// UserRole.java
package com.easybus.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "role_id")
	    private Long roleId;

	    @Column(name = "user_id", nullable = false)
	    private Long userId;

	    @Column(name = "role_name", length = 255, nullable = false)
	    private String roleName;

	    @Lob
	    @Column(name = "permissions")
	    private String permissions;

	    @Column(name = "assigned_at")
	    private LocalDateTime assignedAt;

	    @Column(name = "created_by", length = 255)
	    private String createdBy;

	    @Column(name = "updated_by", length = 255)
	    private String updatedBy;

	    // Duplicate column `roleName` exists, so mapping it as another field
	    @Column(name = "roleName", length = 255)
	    private String roleNameDuplicate;
}
