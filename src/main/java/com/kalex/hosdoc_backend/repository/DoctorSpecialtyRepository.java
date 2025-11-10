package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.DoctorSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorSpecialtyRepository extends JpaRepository<DoctorSpecialty, Long> {
	List<DoctorSpecialty> findByDoctorId(Long doctorId);
	List<DoctorSpecialty> findBySpecialtyId(Long specialtyId);
	void deleteByDoctorId(Long doctorId);
}

