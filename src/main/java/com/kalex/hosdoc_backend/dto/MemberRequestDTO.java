package com.kalex.hosdoc_backend.dto;

import com.kalex.hosdoc_backend.enums.Gender;
import com.kalex.hosdoc_backend.enums.Relation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDTO {
	@NotBlank(message = "Member name is required")
	private String name;
	
	@NotNull(message = "Gender is required")
	private Gender gender;
	
	@NotNull(message = "Date of birth is required")
	private LocalDate dob;
	
	@NotNull(message = "Relation is required")
	private Relation relation;
	
	private String mobile;
	
	@NotNull(message = "Acknowledgement is required")
	private Boolean ackDetails;
}

