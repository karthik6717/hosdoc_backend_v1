package com.kalex.hosdoc_backend.controller;

import com.kalex.hosdoc_backend.dto.AvailabilityDTO;
import com.kalex.hosdoc_backend.dto.DoctorDetailsDTO;
import com.kalex.hosdoc_backend.dto.DoctorSummaryDTO;
import com.kalex.hosdoc_backend.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@Tag(name = "Doctors", description = "Public doctor management APIs")
@CrossOrigin(origins = "*")
public class DoctorController {
	
	@Autowired
	private DoctorService doctorService;
	
	@GetMapping
	@Operation(summary = "Search doctors with filters")
	public ResponseEntity<List<DoctorSummaryDTO>> searchDoctors(
			@RequestParam(required = false) Long specialtyId,
			@RequestParam(required = false) String name) {
		List<DoctorSummaryDTO> doctors = doctorService.searchDoctors(specialtyId, name);
		return ResponseEntity.ok(doctors);
	}
	
	@GetMapping("/{doctorId}")
	@Operation(summary = "Get doctor details by ID")
	public ResponseEntity<DoctorDetailsDTO> getDoctorDetails(@PathVariable Long doctorId) {
		DoctorDetailsDTO doctor = doctorService.getDoctorDetails(doctorId);
		return ResponseEntity.ok(doctor);
	}
	
	@GetMapping("/{doctorId}/availability")
	@Operation(summary = "Get doctor availability")
	public ResponseEntity<AvailabilityDTO> getDoctorAvailability(
			@PathVariable Long doctorId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		if (to == null) {
			to = from.plusDays(7);
		}
		AvailabilityDTO availability = doctorService.getDoctorAvailability(doctorId, from, to);
		return ResponseEntity.ok(availability);
	}
}

