package com.kalex.hosdoc_backend.entity;

import com.kalex.hosdoc_backend.enums.StorageProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "patient_user_id", nullable = false)
	private Integer patientUserId;
	
	@Column(name = "patient_id")
	private Long patientId;
	
	@Column(name = "name", length = 255, nullable = false)
	private String name;
	
	@Column(name = "description", columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "file_url", length = 2048, nullable = false)
	private String fileUrl;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "storage_provider", length = 50)
	private StorageProvider storageProvider = StorageProvider.LOCAL;
	
	@Column(name = "uploaded_at")
	private LocalDateTime uploadedAt;
	
	@Column(name = "created_by", nullable = false)
	private Integer createdBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private Patient patient;
	
	@PrePersist
	protected void onCreate() {
		uploadedAt = LocalDateTime.now();
	}
}

