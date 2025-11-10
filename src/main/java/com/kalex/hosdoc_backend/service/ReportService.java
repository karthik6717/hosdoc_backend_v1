package com.kalex.hosdoc_backend.service;

import com.kalex.hosdoc_backend.dto.ReportDTO;
import com.kalex.hosdoc_backend.dto.ReportUploadRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportService {
	ReportDTO uploadReport(Integer userId, MultipartFile file, ReportUploadRequestDTO request);
	List<ReportDTO> getReportsByUserId(Integer userId);
	ReportDTO getReportById(Long reportId, Integer userId);
}

