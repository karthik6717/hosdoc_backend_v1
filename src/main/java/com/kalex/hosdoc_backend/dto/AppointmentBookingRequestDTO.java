package com.kalex.hosdoc_backend.dto;

import com.kalex.hosdoc_backend.enums.PaymentMode;
import com.kalex.hosdoc_backend.enums.Shift;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentBookingRequestDTO {
	@NotNull(message = "Doctor ID is required")
	private Long doctorId;
	
	@NotNull(message = "Patient member ID is required")
	private Long patientMemberId;
	
	@NotNull(message = "Date is required")
	private LocalDate date;
	
	@NotNull(message = "Shift is required")
	private Shift shift;
	
	@NotNull(message = "Slot time is required")
	private LocalTime slot;
	
	@NotNull(message = "Payment mode is required")
	private PaymentMode paymentMode;
	
	private String notes;
}

