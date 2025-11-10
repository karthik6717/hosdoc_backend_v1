package com.kalex.hosdoc_backend.controller;

import com.kalex.hosdoc_backend.dto.AppointmentBookingRequestDTO;
import com.kalex.hosdoc_backend.dto.AppointmentConfirmationDTO;
import com.kalex.hosdoc_backend.dto.AppointmentDTO;
import com.kalex.hosdoc_backend.service.AppointmentService;
import com.kalex.hosdoc_backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
@Tag(name = "Appointments", description = "Appointment management APIs")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class AppointmentController {
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping
	@Operation(summary = "Book a new appointment")
	public ResponseEntity<AppointmentConfirmationDTO> bookAppointment(@Valid @RequestBody AppointmentBookingRequestDTO request) {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		AppointmentConfirmationDTO confirmation = appointmentService.bookAppointment(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(confirmation);
	}
	
	@GetMapping("/{appointmentUid}")
	@Operation(summary = "Get appointment by UID")
	public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable String appointmentUid) {
		AppointmentDTO appointment = appointmentService.getAppointmentByUid(appointmentUid);
		return ResponseEntity.ok(appointment);
	}
	
	@PutMapping("/{appointmentUid}/cancel")
	@Operation(summary = "Cancel an appointment")
	public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable String appointmentUid) {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		AppointmentDTO appointment = appointmentService.cancelAppointment(userId, appointmentUid);
		return ResponseEntity.ok(appointment);
	}
	
	@PutMapping("/{appointmentUid}/reschedule")
	@Operation(summary = "Reschedule an appointment")
	public ResponseEntity<AppointmentDTO> rescheduleAppointment(
			@PathVariable String appointmentUid,
			@Valid @RequestBody AppointmentBookingRequestDTO newRequest) {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		AppointmentDTO appointment = appointmentService.rescheduleAppointment(userId, appointmentUid, newRequest);
		return ResponseEntity.ok(appointment);
	}
	
	@PostMapping("/{appointmentUid}/pay")
	@Operation(summary = "Process payment for an appointment")
	public ResponseEntity<?> payAppointment(@PathVariable String appointmentUid) {
		// TODO: Implement payment processing
		return ResponseEntity.ok().build();
	}
}

