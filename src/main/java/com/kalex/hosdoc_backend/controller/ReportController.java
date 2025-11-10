package com.kalex.hosdoc_backend.controller;

import com.kalex.hosdoc_backend.dto.ReportDTO;
import com.kalex.hosdoc_backend.dto.ReportUploadRequestDTO;
import com.kalex.hosdoc_backend.service.ReportService;
import com.kalex.hosdoc_backend.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports", description = "Report management APIs")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping(consumes = "multipart/form-data")
	@Operation(summary = "Upload a report")
	public ResponseEntity<ReportDTO> uploadReport(
			@RequestPart("file") MultipartFile file,
			@RequestPart("request") @Valid ReportUploadRequestDTO request) {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		ReportDTO report = reportService.uploadReport(userId, file, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(report);
	}
	
	
	@GetMapping("/{reportId}")
	@Operation(summary = "Get report by ID")
	public ResponseEntity<ReportDTO> getReport(@PathVariable Long reportId) {
		Integer userId = jwtUtil.getCurrentUserIdAsInteger();
		ReportDTO report = reportService.getReportById(reportId, userId);
		return ResponseEntity.ok(report);
	}
}

