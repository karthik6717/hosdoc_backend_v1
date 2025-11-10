package com.kalex.hosdoc_backend.controller;

import com.kalex.hosdoc_backend.dto.MemberCardDTO;
import com.kalex.hosdoc_backend.dto.MemberRequestDTO;
import com.kalex.hosdoc_backend.service.PatientService;
import com.kalex.hosdoc_backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients/me")
@Tag(name = "Patients", description = "Patient and member management APIs")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class PatientController {
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping("/members")
	@Operation(summary = "Get all members for current user")
	public ResponseEntity<List<MemberCardDTO>> getMembers() {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		List<MemberCardDTO> members = patientService.getMembersByUserId(userId);
		return ResponseEntity.ok(members);
	}
	
	@PostMapping("/members")
	@Operation(summary = "Create a new member")
	public ResponseEntity<MemberCardDTO> createMember(@Valid @RequestBody MemberRequestDTO request) {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		MemberCardDTO member = patientService.createMember(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(member);
	}
	
	@PutMapping("/members/{memberId}")
	@Operation(summary = "Update a member")
	public ResponseEntity<MemberCardDTO> updateMember(
			@PathVariable Long memberId,
			@Valid @RequestBody MemberRequestDTO request) {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		MemberCardDTO member = patientService.updateMember(userId, memberId, request);
		return ResponseEntity.ok(member);
	}
	
	@DeleteMapping("/members/{memberId}")
	@Operation(summary = "Delete a member")
	public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		patientService.deleteMember(userId, memberId);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/members/{memberId}")
	@Operation(summary = "Get member by ID")
	public ResponseEntity<MemberCardDTO> getMember(@PathVariable Long memberId) {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		MemberCardDTO member = patientService.getMemberById(userId, memberId);
		return ResponseEntity.ok(member);
	}
}

