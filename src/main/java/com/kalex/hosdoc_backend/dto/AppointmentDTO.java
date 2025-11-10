package com.kalex.hosdoc_backend.dto;

import com.kalex.hosdoc_backend.enums.AppointmentStatus;
import com.kalex.hosdoc_backend.enums.PaymentMode;
import com.kalex.hosdoc_backend.enums.PaymentStatus;
import com.kalex.hosdoc_backend.enums.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
	private String appointmentUid;
	private Long doctorId;
	private String doctorName;
	private String doctorImage;
	private Long patientId;
	private String patientName;
	private LocalDate date;
	private Shift shift;
	private LocalTime slotTime;
	private Integer durationMinutes;
	private BigDecimal fee;
	private PaymentMode paymentMode;
	private PaymentStatus paymentStatus;
	private AppointmentStatus status;
	private String notes;
	private LocalDateTime createdAt;
}

