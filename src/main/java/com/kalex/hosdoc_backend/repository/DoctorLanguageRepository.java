package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.DoctorLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorLanguageRepository extends JpaRepository<DoctorLanguage, Long> {
	List<DoctorLanguage> findByDoctorId(Long doctorId);
	void deleteByDoctorId(Long doctorId);
}

