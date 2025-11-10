package com.kalex.hosdoc_backend.entity;

import com.kalex.hosdoc_backend.enums.Shift;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_availability", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"doctor_id", "date", "shift"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAvailability {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "doctor_id", nullable = false)
	private Long doctorId;
	
	@Column(name = "date", nullable = false)
	private LocalDate date;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "shift", nullable = false, length = 50)
	private Shift shift;
	
	@Column(name = "start_time", nullable = false)
	private LocalTime startTime;
	
	@Column(name = "end_time", nullable = false)
	private LocalTime endTime;
	
	@Column(name = "slot_duration_minutes")
	private Integer slotDurationMinutes = 30;
	
	@Column(name = "is_active")
	private Boolean isActive = true;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	private Doctor doctor;
}

