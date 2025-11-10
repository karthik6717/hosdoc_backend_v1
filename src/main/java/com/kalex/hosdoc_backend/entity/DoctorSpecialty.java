package com.kalex.hosdoc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctor_specialty", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"doctor_id", "specialty_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSpecialty {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "doctor_id", nullable = false)
	private Long doctorId;
	
	@Column(name = "specialty_id", nullable = false)
	private Long specialtyId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	private Doctor doctor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specialty_id", insertable = false, updatable = false)
	private Specialty specialty;
}

