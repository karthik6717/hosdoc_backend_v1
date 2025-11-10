package com.kalex.hosdoc_backend.dto;

import com.kalex.hosdoc_backend.enums.PaymentStatus;
import com.kalex.hosdoc_backend.enums.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentConfirmationDTO {
	private String appointmentUid;
	private String status;
	private Long doctorId;
	private Long patientId;
	private LocalDate date;
	private Shift shift;
	private LocalTime slot;
	private BigDecimal fee;
	private PaymentStatus paymentStatus;
}

