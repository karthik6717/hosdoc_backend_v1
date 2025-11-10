package com.kalex.hosdoc_backend.entity;

import com.kalex.hosdoc_backend.enums.Gender;
import com.kalex.hosdoc_backend.enums.Relation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id", nullable = false)
	private Integer userId; // FK to hosdoc_auth_user.id
	
	@Column(name = "full_name", length = 255)
	private String fullName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "gender", length = 50)
	private Gender gender;
	
	@Column(name = "dob")
	private LocalDate dob;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "relation", length = 50)
	private Relation relation;
	
	@Column(name = "phone", length = 20)
	private String phone;
	
	@Column(name = "avatar_url", length = 1024)
	private String avatarUrl;
	
	@Column(name = "ack_details")
	private Boolean ackDetails = false;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
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

