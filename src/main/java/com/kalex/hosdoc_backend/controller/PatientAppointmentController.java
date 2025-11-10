package com.kalex.hosdoc_backend.controller;

import com.kalex.hosdoc_backend.dto.AppointmentDTO;
import com.kalex.hosdoc_backend.enums.AppointmentStatus;
import com.kalex.hosdoc_backend.service.AppointmentService;
import com.kalex.hosdoc_backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients/me/appointments")
@Tag(name = "Patient Appointments", description = "Patient appointment management APIs")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class PatientAppointmentController {
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping
	@Operation(summary = "Get all appointments for current user")
	public ResponseEntity<List<AppointmentDTO>> getMyAppointments(
			@RequestParam(required = false) AppointmentStatus status) {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		List<AppointmentDTO> appointments = appointmentService.getAppointmentsByUserId(userId, status);
		return ResponseEntity.ok(appointments);
	}
}

