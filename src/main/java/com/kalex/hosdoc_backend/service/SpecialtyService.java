package com.kalex.hosdoc_backend.service;

import java.util.List;

import com.kalex.hosdoc_backend.entity.Specialty;

public interface SpecialtyService {
	List<Specialty> getAllSpecialties();
	Specialty getSpecialtyById(Long id);
}

