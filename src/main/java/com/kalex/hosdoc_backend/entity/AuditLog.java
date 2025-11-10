package com.kalex.hosdoc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "entity_type", length = 100)
	private String entityType;
	
	@Column(name = "entity_id", length = 100)
	private String entityId;
	
	@Column(name = "action", length = 100)
	private String action;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "detail", columnDefinition = "JSON")
	private Map<String, Object> detail;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
}

