package com.kalex.hosdoc_backend.service;

import com.kalex.hosdoc_backend.dto.ReportDTO;
import com.kalex.hosdoc_backend.dto.ReportUploadRequestDTO;
import com.kalex.hosdoc_backend.entity.Patient;
import com.kalex.hosdoc_backend.entity.Report;
import com.kalex.hosdoc_backend.enums.StorageProvider;
import com.kalex.hosdoc_backend.exception.ResourceNotFoundException;
import com.kalex.hosdoc_backend.repository.PatientRepository;
import com.kalex.hosdoc_backend.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	@Value("${app.file.upload-dir:./uploads/reports}")
	private String uploadDir;
	
	@Override
	public ReportDTO uploadReport(Integer userId, MultipartFile file, ReportUploadRequestDTO request) {
		if (file.isEmpty()) {
			throw new IllegalArgumentException("File is empty");
		}
		
		// Validate file size (e.g., max 10MB)
		if (file.getSize() > 10 * 1024 * 1024) {
			throw new IllegalArgumentException("File size exceeds maximum limit");
		}
		
		try {
			// Create upload directory if it doesn't exist
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			// Generate unique filename
			String originalFilename = file.getOriginalFilename();
			String fileExtension = originalFilename != null && originalFilename.contains(".") 
				? originalFilename.substring(originalFilename.lastIndexOf(".")) 
				: "";
			String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
			Path filePath = uploadPath.resolve(uniqueFilename);
			
			// Save file
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			
			// Determine patient ID
			Long patientId = request.getPatientId();
			if (patientId == null) {
				// Get patient record for user
				List<Patient> patients = patientRepository.findByUserId(userId);
				if (!patients.isEmpty()) {
					patientId = patients.get(0).getId(); // Use first patient record
				}
			}
			
			// Create report entity
			Report report = new Report();
			report.setPatientUserId(userId);
			report.setPatientId(patientId);
			report.setName(request.getName());
			report.setDescription(request.getDescription());
			report.setFileUrl(filePath.toString()); // In production, use S3 URL
			report.setStorageProvider(StorageProvider.LOCAL);
			report.setCreatedBy(userId);
			
			Report saved = reportRepository.save(report);
			return convertToDTO(saved);
			
		} catch (IOException e) {
			throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
		}
	}
	
	@Override
	public List<ReportDTO> getReportsByUserId(Integer userId) {
		List<Report> reports = reportRepository.findByPatientUserId(userId);
		return reports.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}
	
	@Override
	public ReportDTO getReportById(Long reportId, Integer userId) {
		Report report = reportRepository.findById(reportId)
			.orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
		
		if (!report.getPatientUserId().equals(userId)) {
			throw new ResourceNotFoundException("Report not found");
		}
		
		return convertToDTO(report);
	}
	
	private ReportDTO convertToDTO(Report report) {
		ReportDTO dto = new ReportDTO();
		dto.setReportId(report.getId());
		dto.setName(report.getName());
		dto.setDescription(report.getDescription());
		dto.setFileUrl(report.getFileUrl());
		dto.setUploadedAt(report.getUploadedAt());
		dto.setPatientId(report.getPatientId());
		
		if (report.getPatientId() != null) {
			Patient patient = patientRepository.findById(report.getPatientId()).orElse(null);
			if (patient != null) {
				dto.setPatientName(patient.getFullName());
			}
		}
		
		return dto;
	}
}

