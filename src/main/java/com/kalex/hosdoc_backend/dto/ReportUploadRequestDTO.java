package com.kalex.hosdoc_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportUploadRequestDTO {
	@NotBlank(message = "Report name is required")
	private String name;
	
	private String description;
	
	private Long patientId; // Optional, defaults to current user's patient record
}

