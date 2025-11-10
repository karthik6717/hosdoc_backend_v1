package com.kalex.hosdoc_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
	private Long reportId;
	private String name;
	private String description;
	private String fileUrl;
	private LocalDateTime uploadedAt;
	private Long patientId;
	private String patientName;
}

