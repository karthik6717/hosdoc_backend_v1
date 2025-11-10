package com.kalex.hosdoc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id", nullable = false)
	private Integer userId; // FK to hosdoc_auth_user.id
	
	@Column(name = "display_name", length = 255)
	private String displayName;
	
	@Column(name = "slug", length = 255)
	private String slug;
	
	@Column(name = "about", columnDefinition = "TEXT")
	private String about;
	
	@Column(name = "fee", precision = 10, scale = 2)
	private BigDecimal fee;
	
	@Column(name = "years_of_experience")
	private Integer yearsOfExperience;
	
	@Column(name = "rating", precision = 3, scale = 2)
	private BigDecimal rating;
	
	@Column(name = "profile_image_url", length = 1024)
	private String profileImageUrl;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<DoctorSpecialty> specialties = new ArrayList<>();
	
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
