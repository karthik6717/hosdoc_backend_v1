package com.kalex.hosdoc_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kalex.hosdoc_backend.repository.SpecialtyRepository;
import com.kalex.hosdoc_backend.entity.Specialty;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

	@Autowired
	private SpecialtyRepository specialtyRepository;

	@Override
	public List<Specialty> getAllSpecialties() {
		return specialtyRepository.findAll();
	}
	
	public Specialty getSpecialtyById(Long id) {
		return specialtyRepository.findById(id)
			.orElseThrow(() -> new com.kalex.hosdoc_backend.exception.ResourceNotFoundException("Specialty not found with id: " + id));
	}
}

