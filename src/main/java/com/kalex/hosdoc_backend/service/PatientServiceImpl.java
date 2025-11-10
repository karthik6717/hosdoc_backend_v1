package com.kalex.hosdoc_backend.service;

import com.kalex.hosdoc_backend.dto.MemberCardDTO;
import com.kalex.hosdoc_backend.dto.MemberRequestDTO;
import com.kalex.hosdoc_backend.entity.Patient;
import com.kalex.hosdoc_backend.exception.BadRequestException;
import com.kalex.hosdoc_backend.exception.ResourceNotFoundException;
import com.kalex.hosdoc_backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {
	
	@Autowired
	private PatientRepository patientRepository;
	
	@Override
	public List<MemberCardDTO> getMembersByUserId(Integer userId) {
		List<Patient> patients = patientRepository.findByUserId(userId);
		return patients.stream()
			.map(this::convertToMemberCardDTO)
			.collect(Collectors.toList());
	}
	
	@Override
	public MemberCardDTO createMember(Integer userId, MemberRequestDTO request) {
		if (!Boolean.TRUE.equals(request.getAckDetails())) {
			throw new BadRequestException("Acknowledgement is required to save member");
		}
		
		Patient patient = new Patient();
		patient.setUserId(userId);
		patient.setFullName(request.getName());
		patient.setGender(request.getGender());
		patient.setDob(request.getDob());
		patient.setRelation(request.getRelation());
		patient.setPhone(request.getMobile());
		patient.setAckDetails(request.getAckDetails());
		
		Patient saved = patientRepository.save(patient);
		return convertToMemberCardDTO(saved);
	}
	
	@Override
	public MemberCardDTO updateMember(Integer userId, Long memberId, MemberRequestDTO request) {
		Patient patient = patientRepository.findByIdAndUserId(memberId, userId)
			.orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
		
		if (!Boolean.TRUE.equals(request.getAckDetails())) {
			throw new BadRequestException("Acknowledgement is required to save member");
		}
		
		patient.setFullName(request.getName());
		patient.setGender(request.getGender());
		patient.setDob(request.getDob());
		patient.setRelation(request.getRelation());
		patient.setPhone(request.getMobile());
		patient.setAckDetails(request.getAckDetails());
		
		Patient saved = patientRepository.save(patient);
		return convertToMemberCardDTO(saved);
	}
	
	@Override
	public void deleteMember(Integer userId, Long memberId) {
		Patient patient = patientRepository.findByIdAndUserId(memberId, userId)
			.orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
		patientRepository.delete(patient);
	}
	
	@Override
	public MemberCardDTO getMemberById(Integer userId, Long memberId) {
		Patient patient = patientRepository.findByIdAndUserId(memberId, userId)
			.orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
		return convertToMemberCardDTO(patient);
	}
	
	private MemberCardDTO convertToMemberCardDTO(Patient patient) {
		MemberCardDTO dto = new MemberCardDTO();
		dto.setMemberId(patient.getId());
		dto.setName(patient.getFullName());
		dto.setRelation(patient.getRelation());
		dto.setGender(patient.getGender());
		dto.setDob(patient.getDob());
		dto.setAvatarUrl(patient.getAvatarUrl());
		
		// Calculate age
		if (patient.getDob() != null) {
			int age = Period.between(patient.getDob(), LocalDate.now()).getYears();
			dto.setAge(age);
		}
		
		return dto;
	}
}

