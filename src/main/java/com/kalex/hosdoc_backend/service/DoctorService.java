package com.kalex.hosdoc_backend.service;

import com.kalex.hosdoc_backend.dto.DoctorDetailsDTO;
import com.kalex.hosdoc_backend.dto.DoctorSummaryDTO;
import com.kalex.hosdoc_backend.dto.AvailabilityDTO;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
	List<DoctorSummaryDTO> getDoctorsBySpecialty(Long specialtyId);
	List<DoctorSummaryDTO> searchDoctors(Long specialtyId, String name);
	DoctorDetailsDTO getDoctorDetails(Long doctorId);
	AvailabilityDTO getDoctorAvailability(Long doctorId, LocalDate from, LocalDate to);
}

