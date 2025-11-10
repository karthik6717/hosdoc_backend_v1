package com.kalex.hosdoc_backend.entity;

import com.kalex.hosdoc_backend.model.SlotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_slot", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"doctor_id", "date", "slot_time"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSlot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "doctor_id", nullable = false)
	private Long doctorId;
	
	@Column(name = "date", nullable = false)
	private LocalDate date;
	
	@Column(name = "slot_time", nullable = false)
	private LocalTime slotTime;
	
	@Column(name = "duration_minutes")
	private Integer durationMinutes = 30;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 50)
	private SlotStatus status = SlotStatus.AVAILABLE;
	
	@Column(name = "appointment_id")
	private Long appointmentId; // Nullable, set when booked
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	private Doctor doctor;
}
