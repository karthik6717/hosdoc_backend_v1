package com.kalex.hosdoc_backend.controller;

import com.kalex.hosdoc_backend.dto.DoctorSummaryDTO;
import com.kalex.hosdoc_backend.entity.Specialty;
import com.kalex.hosdoc_backend.service.DoctorService;
import com.kalex.hosdoc_backend.service.SpecialtyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/specialties")
@Tag(name = "Specialties", description = "Public specialty management APIs")
@CrossOrigin(origins = "*")
public class SpecialtyController {

	@Autowired
	private SpecialtyService specialtyService;
	
	@Autowired
	private DoctorService doctorService;

	@GetMapping
	@Operation(summary = "Get all specialties")
	public ResponseEntity<List<Specialty>> getAllSpecialties() {
		List<Specialty> specialties = specialtyService.getAllSpecialties();
		return ResponseEntity.ok(specialties);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get specialty by ID")
	public ResponseEntity<Specialty> getSpecialtyById(@PathVariable Long id) {
		Specialty specialty = specialtyService.getSpecialtyById(id);
		return ResponseEntity.ok(specialty);
	}
	
	@GetMapping("/{id}/doctors")
	@Operation(summary = "Get doctors by specialty")
	public ResponseEntity<List<DoctorSummaryDTO>> getDoctorsBySpecialty(@PathVariable Long id) {
		List<DoctorSummaryDTO> doctors = doctorService.getDoctorsBySpecialty(id);
		return ResponseEntity.ok(doctors);
	}
}
