package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.DoctorAvailability;
import com.kalex.hosdoc_backend.enums.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
	List<DoctorAvailability> findByDoctorIdAndDateBetween(Long doctorId, LocalDate from, LocalDate to);
	Optional<DoctorAvailability> findByDoctorIdAndDateAndShift(Long doctorId, LocalDate date, Shift shift);
	List<DoctorAvailability> findByDoctorId(Long doctorId);
}

