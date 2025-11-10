package com.kalex.hosdoc_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "language_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LanguageMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "code", length = 50, unique = true)
	private String code;
	
	@Column(name = "name", length = 100, nullable = false)
	private String name;
}
