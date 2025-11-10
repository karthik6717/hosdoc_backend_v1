package com.kalex.hosdoc_backend.controller;

import com.kalex.hosdoc_backend.dto.ReportDTO;
import com.kalex.hosdoc_backend.service.ReportService;
import com.kalex.hosdoc_backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients/me/reports")
@Tag(name = "Patient Reports", description = "Patient report management APIs")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class PatientReportController {
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@GetMapping
	@Operation(summary = "Get all reports for current user")
	public ResponseEntity<List<ReportDTO>> getMyReports() {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		List<ReportDTO> reports = reportService.getReportsByUserId(userId);
		return ResponseEntity.ok(reports);
	}
}

