package com.kalex.hosdoc_backend.service;

import com.kalex.hosdoc_backend.dto.AvailabilityDTO;
import com.kalex.hosdoc_backend.dto.DoctorDetailsDTO;
import com.kalex.hosdoc_backend.dto.DoctorSummaryDTO;
import com.kalex.hosdoc_backend.dto.QualificationDTO;
import com.kalex.hosdoc_backend.entity.*;
import com.kalex.hosdoc_backend.enums.Shift;
import com.kalex.hosdoc_backend.exception.ResourceNotFoundException;
import com.kalex.hosdoc_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private DoctorSpecialtyRepository doctorSpecialtyRepository;
	
	@Autowired
	private SpecialtyRepository specialtyRepository;
	
	@Autowired
	private QualificationRepository qualificationRepository;
	
	@Autowired
	private DoctorLanguageRepository doctorLanguageRepository;
	
	@Autowired
	private LanguageMasterRepository languageMasterRepository;
	
	@Autowired
	private DoctorAvailabilityRepository doctorAvailabilityRepository;
	
	@Autowired
	private DoctorSlotRepository doctorSlotRepository;
	
	
	@Override
	public List<DoctorSummaryDTO> getDoctorsBySpecialty(Long specialtyId) {
		List<Doctor> doctors = doctorRepository.findBySpecialtyId(specialtyId);
		return doctors.stream()
			.map(this::convertToSummaryDTO)
			.collect(Collectors.toList());
	}
	
	@Override
	public List<DoctorSummaryDTO> searchDoctors(Long specialtyId, String name) {
		List<Doctor> doctors = doctorRepository.findWithFilters(specialtyId, name);
		return doctors.stream()
			.map(this::convertToSummaryDTO)
			.collect(Collectors.toList());
	}
	
	@Override
	public DoctorDetailsDTO getDoctorDetails(Long doctorId) {
		Doctor doctor = doctorRepository.findById(doctorId)
			.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
		
		DoctorDetailsDTO dto = new DoctorDetailsDTO();
		dto.setDoctorId(doctor.getId());
		dto.setName(doctor.getDisplayName());
		dto.setAbout(doctor.getAbout());
		dto.setFee(doctor.getFee());
		dto.setYearsOfExperience(doctor.getYearsOfExperience());
		dto.setRating(doctor.getRating());
		dto.setProfileImageUrl(doctor.getProfileImageUrl());
		
		// Get specializations
		List<DoctorSpecialty> doctorSpecialties = doctorSpecialtyRepository.findByDoctorId(doctorId);
		List<String> specializations = doctorSpecialties.stream()
			.map(ds -> {
				Specialty specialty = specialtyRepository.findById(ds.getSpecialtyId()).orElse(null);
				return specialty != null ? specialty.getName() : null;
			})
			.filter(s -> s != null)
			.collect(Collectors.toList());
		dto.setSpecializations(specializations);
		
		// Get qualifications
		List<Qualification> qualifications = qualificationRepository.findByDoctorId(doctorId);
		List<QualificationDTO> qualificationDTOs = qualifications.stream()
			.map(q -> new QualificationDTO(q.getQualification(), q.getInstitution(), q.getYear()))
			.collect(Collectors.toList());
		dto.setQualifications(qualificationDTOs);
		
		// Get languages
		List<DoctorLanguage> doctorLanguages = doctorLanguageRepository.findByDoctorId(doctorId);
		List<String> languages = doctorLanguages.stream()
			.map(dl -> {
				LanguageMaster language = languageMasterRepository.findById(dl.getLanguageId()).orElse(null);
				return language != null ? language.getName() : null;
			})
			.filter(l -> l != null)
			.collect(Collectors.toList());
		dto.setLanguages(languages);
		
		return dto;
	}
	
	@Override
	public AvailabilityDTO getDoctorAvailability(Long doctorId, LocalDate from, LocalDate to) {
		doctorRepository.findById(doctorId)
			.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
		
		if (to == null) {
			to = from.plusDays(7); // Default to 7 days ahead
		}
		
		AvailabilityDTO availabilityDTO = new AvailabilityDTO();
		availabilityDTO.setDate(from);
		
		List<DoctorAvailability> availabilities = doctorAvailabilityRepository
			.findByDoctorIdAndDateBetween(doctorId, from, to);
		
		// Group by date and shift
		List<AvailabilityDTO.ShiftAvailabilityDTO> shiftAvailabilities = new ArrayList<>();
		
		LocalDate currentDate = from;
		while (!currentDate.isAfter(to)) {
			final LocalDate date = currentDate;
			for (Shift shift : Shift.values()) {
				DoctorAvailability availability = availabilities.stream()
					.filter(a -> a.getDate().equals(date) && a.getShift() == shift && Boolean.TRUE.equals(a.getIsActive()))
					.findFirst()
					.orElse(null);
				
				if (availability != null) {
					AvailabilityDTO.ShiftAvailabilityDTO shiftDTO = new AvailabilityDTO.ShiftAvailabilityDTO();
					shiftDTO.setShift(shift);
					shiftDTO.setStartTime(availability.getStartTime());
					shiftDTO.setEndTime(availability.getEndTime());
					
					// Get available slots
					List<DoctorSlot> slots = doctorSlotRepository.findByDoctorIdAndDateAndStatus(
						doctorId, date, com.kalex.hosdoc_backend.model.SlotStatus.AVAILABLE);
					
					List<AvailabilityDTO.SlotDTO> slotDTOs = slots.stream()
						.filter(s -> isSlotInShift(s.getSlotTime(), availability.getStartTime(), availability.getEndTime()))
						.map(s -> {
							AvailabilityDTO.SlotDTO slotDTO = new AvailabilityDTO.SlotDTO();
							slotDTO.setSlotTime(s.getSlotTime());
							slotDTO.setAvailable(true);
							return slotDTO;
						})
						.sorted(Comparator.comparing(AvailabilityDTO.SlotDTO::getSlotTime))
						.collect(Collectors.toList());
					
					shiftDTO.setAvailableSlots(slotDTOs);
					shiftAvailabilities.add(shiftDTO);
				}
			}
			currentDate = currentDate.plusDays(1);
		}
		
		availabilityDTO.setShifts(shiftAvailabilities);
		return availabilityDTO;
	}
	
	private boolean isSlotInShift(LocalTime slotTime, LocalTime shiftStart, LocalTime shiftEnd) {
		return !slotTime.isBefore(shiftStart) && !slotTime.isAfter(shiftEnd);
	}
	
	private DoctorSummaryDTO convertToSummaryDTO(Doctor doctor) {
		DoctorSummaryDTO dto = new DoctorSummaryDTO();
		dto.setDoctorId(doctor.getId());
		dto.setName(doctor.getDisplayName());
		dto.setProfileImage(doctor.getProfileImageUrl());
		dto.setYearsOfExperience(doctor.getYearsOfExperience());
		dto.setFee(doctor.getFee());
		
		// Get specializations
		List<DoctorSpecialty> doctorSpecialties = doctorSpecialtyRepository.findByDoctorId(doctor.getId());
		List<String> specializations = doctorSpecialties.stream()
			.map(ds -> {
				Specialty specialty = specialtyRepository.findById(ds.getSpecialtyId()).orElse(null);
				return specialty != null ? specialty.getName() : null;
			})
			.filter(s -> s != null)
			.collect(Collectors.toList());
		dto.setSpecializations(specializations);
		
		// Get languages
		List<DoctorLanguage> doctorLanguages = doctorLanguageRepository.findByDoctorId(doctor.getId());
		List<String> languages = doctorLanguages.stream()
			.map(dl -> {
				LanguageMaster language = languageMasterRepository.findById(dl.getLanguageId()).orElse(null);
				return language != null ? language.getName() : null;
			})
			.filter(l -> l != null)
			.collect(Collectors.toList());
		dto.setLanguages(languages);
		
		// Get qualification summary
		List<Qualification> qualifications = qualificationRepository.findByDoctorId(doctor.getId());
		String qualificationSummary = qualifications.stream()
			.map(Qualification::getQualification)
			.collect(Collectors.joining(", "));
		dto.setQualificationSummary(qualificationSummary);
		
		// TODO: Calculate next available slot
		// dto.setNextAvailable(...);
		
		return dto;
	}
}

