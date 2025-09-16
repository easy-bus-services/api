package com.easybus.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Data
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id", nullable = false)
	private Long roleId; 
	@Column(name = "role_name", nullable = false,unique = true)
	private String name; // e.g. ROLE_ADMIN, ROLE_USER

	 @Column(name = "user_id", nullable = false)
	 private Long userId;



	@Column(name = "permissions")
	private String permissions; // Comma-separated permissions

	
	  
	public Set<String> getPermissionsSet() {
        if (permissions == null || permissions.isEmpty()) return new HashSet<>();
        return Arrays.stream(permissions.split(","))
                     .map(String::trim)
                     .collect(Collectors.toSet());
    }

    public void setPermissionsSet(Set<String> permissionSet) {
        this.permissions = String.join(",", permissionSet);
    }
    
    @Column(name = "assigned_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime assignedAt;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
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
	
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "role_permissions",
//             inverseJoinColumns = @JoinColumn(name = "permission_id")   )
//    private Set<Permission> permissions = new HashSet<>();
    
//    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private Set<RolePermission> rolePermissions = new HashSet<>();

}
