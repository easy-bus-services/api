
package com.easybus.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_roles")
@Data
public class UserRole {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long userRoleId;

	    @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    @ManyToOne
	    @JoinColumn(name = "role_id", nullable = false)
	    private Role role;

	    @Column(name = "assigned_at", updatable = false)
	    private LocalDateTime assignedAt = LocalDateTime.now();

	    @Column(name = "deleted_at")
	    private LocalDateTime deletedAt;
	    @Column(name = "update_date")
		private LocalDateTime updatedDate;
		
		@CreatedBy
	    @Column(name = "created_by", length = 255)
	    private String createdBy;
		@LastModifiedBy
	    @Column(name = "updated_by", length = 255)
	    private String updatedBy;
	
		@Column(name = "deleted_by")
		private String deletedBy;
		

}
