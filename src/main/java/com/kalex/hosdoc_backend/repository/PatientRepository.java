package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
	List<Patient> findByUserId(Integer userId);
	Optional<Patient> findByIdAndUserId(Long id, Integer userId);
}

