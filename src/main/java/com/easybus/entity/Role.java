package com.easybus.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {


	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "role_id")
		private Long roleId; 

	    @Column(name = "role_name", unique = true, nullable = false)
	    private String roleName;

	    @Column(name = "assigned_at", updatable = false, insertable = false,
	            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	    private LocalDateTime assignedAt;

	    @Column(name = "is_deleted")
	    private Boolean isDeleted = false;

	    @Column(name = "created_by")
	    private String createdBy;

	    @Column(name = "updated_by")
	    private String updatedBy;

	    @Column(name = "deleted_by")
	    private String deletedBy;

	    @Column(name = "updated_at", insertable = false,
	            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	    private LocalDateTime updatedAt;

	    @Column(name = "deleted_at")
	    private LocalDateTime deletedAt;
	
	    @ManyToMany(fetch = FetchType.LAZY)
	    @JoinTable(
	        name = "role_permissions",
	        joinColumns = @JoinColumn(name = "role_id"),
	        inverseJoinColumns = @JoinColumn(name = "permission_id")
	    )
	    private Set<Permission> permissions = new HashSet<>();

}
