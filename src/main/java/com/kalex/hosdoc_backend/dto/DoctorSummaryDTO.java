package com.kalex.hosdoc_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSummaryDTO {
	private Long doctorId;
	private String name;
	private List<String> specializations;
	private String profileImage;
	private Integer yearsOfExperience;
	private List<String> languages;
	private BigDecimal fee;
	private String qualificationSummary;
	private LocalDateTime nextAvailable;
}

