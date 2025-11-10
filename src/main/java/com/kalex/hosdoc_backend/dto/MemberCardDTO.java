package com.kalex.hosdoc_backend.dto;

import com.kalex.hosdoc_backend.enums.Gender;
import com.kalex.hosdoc_backend.enums.Relation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCardDTO {
	private Long memberId;
	private String name;
	private Relation relation;
	private Gender gender;
	private LocalDate dob;
	private Integer age;
	private String avatarUrl;
}

