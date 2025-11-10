package com.kalex.hosdoc_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDetailsDTO {
	private Long doctorId;
	private String name;
	private String about;
	private List<String> specializations;
	private List<QualificationDTO> qualifications;
	private Integer yearsOfExperience;
	private List<String> languages;
	private BigDecimal fee;
	private String profileImageUrl;
	private BigDecimal rating;
}

