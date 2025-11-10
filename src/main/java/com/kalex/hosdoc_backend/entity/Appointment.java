package com.kalex.hosdoc_backend.entity;

import com.kalex.hosdoc_backend.enums.AppointmentStatus;
import com.kalex.hosdoc_backend.enums.PaymentMode;
import com.kalex.hosdoc_backend.enums.PaymentStatus;
import com.kalex.hosdoc_backend.enums.Shift;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointment", indexes = {
	@Index(name = "idx_appointment_uid", columnList = "appointment_uid"),
	@Index(name = "idx_doctor_date", columnList = "doctor_id, date"),
	@Index(name = "idx_patient", columnList = "patient_id"),
	@Index(name = "idx_booked_by", columnList = "booked_by_user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "appointment_uid", unique = true, length = 50, nullable = false)
	private String appointmentUid; // HOS-YYYYMMDD-000001
	
	@Column(name = "doctor_id", nullable = false)
	private Long doctorId;
	
	@Column(name = "patient_id", nullable = false)
	private Long patientId;
	
	@Column(name = "booked_by_user_id", nullable = false)
	private Integer bookedByUserId;
	
	@Column(name = "date", nullable = false)
	private LocalDate date;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "shift", nullable = false, length = 50)
	private Shift shift;
	
	@Column(name = "slot_time", nullable = false)
	private LocalTime slotTime;
	
	@Column(name = "duration_minutes")
	private Integer durationMinutes = 30;
	
	@Column(name = "fee", precision = 10, scale = 2, nullable = false)
	private BigDecimal fee;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_mode", nullable = false, length = 50)
	private PaymentMode paymentMode;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status", nullable = false, length = 50)
	private PaymentStatus paymentStatus = PaymentStatus.PENDING;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 50)
	private AppointmentStatus status = AppointmentStatus.CONFIRMED;
	
	@Column(name = "notes", columnDefinition = "TEXT")
	private String notes;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	private Doctor doctor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private Patient patient;
	
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

