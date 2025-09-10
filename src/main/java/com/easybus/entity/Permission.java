package com.easybus.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "permissions")
@Data
public class Permission { @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "permission_uuid", nullable = false, unique = true,length = 255)
    private String permissionUuid;

    
    @Column(name = "permission_name", nullable = false, length = 255)
    private String permissionName;

    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive = true;

    // ---------------- Parent-Child Mapping ----------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_parent")
    @JsonIgnoreProperties({"children", "parent"}) // Avoid infinite recursion in JSON
    private Permission parent;

    @Transient  // not stored in DB
    private Long parentId;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"parent", "children"}) // Avoid recursion in JSON
    private List<Permission> children = new ArrayList<>();
//  public Permission() {
//  this.permissionUuid = UUID.randomUUID(); // auto-generate UUID
//}
}
