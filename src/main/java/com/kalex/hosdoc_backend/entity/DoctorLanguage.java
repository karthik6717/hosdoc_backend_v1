package com.kalex.hosdoc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctor_language")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorLanguage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "doctor_id", nullable = false)
	private Long doctorId;
	
	@Column(name = "language_id", nullable = false)
	private Long languageId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	private Doctor doctor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_id", insertable = false, updatable = false)
	private LanguageMaster language;
}

