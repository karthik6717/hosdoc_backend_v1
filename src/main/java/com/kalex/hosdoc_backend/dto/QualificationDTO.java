package com.kalex.hosdoc_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QualificationDTO {
	private String degree;
	private String institution;
	private Integer year;
}

