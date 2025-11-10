package com.kalex.hosdoc_backend.service;

import com.kalex.hosdoc_backend.dto.AppointmentBookingRequestDTO;
import com.kalex.hosdoc_backend.dto.AppointmentConfirmationDTO;
import com.kalex.hosdoc_backend.dto.AppointmentDTO;
import com.kalex.hosdoc_backend.entity.*;
import com.kalex.hosdoc_backend.enums.AppointmentStatus;
import com.kalex.hosdoc_backend.enums.PaymentStatus;
import com.kalex.hosdoc_backend.exception.BadRequestException;
import com.kalex.hosdoc_backend.exception.ResourceNotFoundException;
import com.kalex.hosdoc_backend.repository.*;
import com.kalex.hosdoc_backend.util.AppointmentIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private DoctorSlotRepository doctorSlotRepository;
	
	@Autowired
	private AppointmentIdGenerator appointmentIdGenerator;
	
	@Override
	public AppointmentConfirmationDTO bookAppointment(Integer userId, AppointmentBookingRequestDTO request) {
		// Validate doctor exists
		Doctor doctor = doctorRepository.findById(request.getDoctorId())
			.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
		
		// Validate patient member exists and belongs to user
		patientRepository.findByIdAndUserId(request.getPatientMemberId(), userId)
			.orElseThrow(() -> new ResourceNotFoundException("Patient member not found"));
		
		// Check and lock slot (prevent double booking)
		DoctorSlot slot = doctorSlotRepository.findAvailableSlotForBooking(
			request.getDoctorId(), request.getDate(), request.getSlot())
			.orElseThrow(() -> new BadRequestException("Slot is not available"));
		
		// Create appointment
		Appointment appointment = new Appointment();
		appointment.setDoctorId(request.getDoctorId());
		appointment.setPatientId(request.getPatientMemberId());
		appointment.setBookedByUserId(userId);
		appointment.setDate(request.getDate());
		appointment.setShift(request.getShift());
		appointment.setSlotTime(request.getSlot());
		appointment.setDurationMinutes(slot.getDurationMinutes());
		appointment.setFee(doctor.getFee());
		appointment.setPaymentMode(request.getPaymentMode());
		appointment.setPaymentStatus(PaymentStatus.PENDING);
		appointment.setStatus(AppointmentStatus.CONFIRMED);
		appointment.setNotes(request.getNotes());
		
		// Generate appointment UID
		long sequenceNumber = appointmentRepository.count() + 1;
		appointment.setAppointmentUid(appointmentIdGenerator.generateAppointmentId(sequenceNumber));
		
		Appointment saved = appointmentRepository.save(appointment);
		
		// Update slot status
		doctorSlotRepository.updateSlotStatus(slot.getId(), 
			com.kalex.hosdoc_backend.model.SlotStatus.BOOKED, saved.getId());
		
		// Return confirmation
		AppointmentConfirmationDTO confirmation = new AppointmentConfirmationDTO();
		confirmation.setAppointmentUid(saved.getAppointmentUid());
		confirmation.setStatus(saved.getStatus().name());
		confirmation.setDoctorId(saved.getDoctorId());
		confirmation.setPatientId(saved.getPatientId());
		confirmation.setDate(saved.getDate());
		confirmation.setShift(saved.getShift());
		confirmation.setSlot(saved.getSlotTime());
		confirmation.setFee(saved.getFee());
		confirmation.setPaymentStatus(saved.getPaymentStatus());
		
		return confirmation;
	}
	
	@Override
	public AppointmentDTO getAppointmentByUid(String appointmentUid) {
		Appointment appointment = appointmentRepository.findByAppointmentUid(appointmentUid)
			.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with UID: " + appointmentUid));
		return convertToDTO(appointment);
	}
	
	@Override
	public List<AppointmentDTO> getAppointmentsByUserId(Integer userId, AppointmentStatus status) {
		List<Appointment> appointments = appointmentRepository.findByBookedByUserIdWithStatus(userId, status);
		return appointments.stream()
			.map(this::convertToDTO)
			.collect(Collectors.toList());
	}
	
	@Override
	public AppointmentDTO cancelAppointment(Integer userId, String appointmentUid) {
		Appointment appointment = appointmentRepository.findByAppointmentUid(appointmentUid)
			.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with UID: " + appointmentUid));
		
		if (!appointment.getBookedByUserId().equals(userId)) {
			throw new BadRequestException("You don't have permission to cancel this appointment");
		}
		
		if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
			throw new BadRequestException("Appointment is already cancelled");
		}
		
		appointment.setStatus(AppointmentStatus.CANCELLED);
		Appointment saved = appointmentRepository.save(appointment);
		
		// Release the slot
		DoctorSlot slot = doctorSlotRepository.findByDoctorIdAndDateAndSlotTime(
			appointment.getDoctorId(), appointment.getDate(), appointment.getSlotTime())
			.orElse(null);
		if (slot != null) {
			doctorSlotRepository.updateSlotStatus(slot.getId(), 
				com.kalex.hosdoc_backend.model.SlotStatus.AVAILABLE, null);
		}
		
		return convertToDTO(saved);
	}
	
	@Override
	public AppointmentDTO rescheduleAppointment(Integer userId, String appointmentUid, AppointmentBookingRequestDTO newRequest) {
		Appointment appointment = appointmentRepository.findByAppointmentUid(appointmentUid)
			.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with UID: " + appointmentUid));
		
		if (!appointment.getBookedByUserId().equals(userId)) {
			throw new BadRequestException("You don't have permission to reschedule this appointment");
		}
		
		// Release old slot
		DoctorSlot oldSlot = doctorSlotRepository.findByDoctorIdAndDateAndSlotTime(
			appointment.getDoctorId(), appointment.getDate(), appointment.getSlotTime())
			.orElse(null);
		if (oldSlot != null) {
			doctorSlotRepository.updateSlotStatus(oldSlot.getId(), 
				com.kalex.hosdoc_backend.model.SlotStatus.AVAILABLE, null);
		}
		
		// Check and lock new slot
		DoctorSlot newSlot = doctorSlotRepository.findAvailableSlotForBooking(
			newRequest.getDoctorId(), newRequest.getDate(), newRequest.getSlot())
			.orElseThrow(() -> new BadRequestException("New slot is not available"));
		
		// Update appointment
		appointment.setDate(newRequest.getDate());
		appointment.setShift(newRequest.getShift());
		appointment.setSlotTime(newRequest.getSlot());
		appointment.setNotes(newRequest.getNotes());
		
		Appointment saved = appointmentRepository.save(appointment);
		
		// Update new slot status
		doctorSlotRepository.updateSlotStatus(newSlot.getId(), 
			com.kalex.hosdoc_backend.model.SlotStatus.BOOKED, saved.getId());
		
		return convertToDTO(saved);
	}
	
	private AppointmentDTO convertToDTO(Appointment appointment) {
		AppointmentDTO dto = new AppointmentDTO();
		dto.setAppointmentUid(appointment.getAppointmentUid());
		dto.setDoctorId(appointment.getDoctorId());
		dto.setPatientId(appointment.getPatientId());
		dto.setDate(appointment.getDate());
		dto.setShift(appointment.getShift());
		dto.setSlotTime(appointment.getSlotTime());
		dto.setDurationMinutes(appointment.getDurationMinutes());
		dto.setFee(appointment.getFee());
		dto.setPaymentMode(appointment.getPaymentMode());
		dto.setPaymentStatus(appointment.getPaymentStatus());
		dto.setStatus(appointment.getStatus());
		dto.setNotes(appointment.getNotes());
		dto.setCreatedAt(appointment.getCreatedAt());
		
		// Load doctor details
		Doctor doctor = doctorRepository.findById(appointment.getDoctorId()).orElse(null);
		if (doctor != null) {
			dto.setDoctorName(doctor.getDisplayName());
			dto.setDoctorImage(doctor.getProfileImageUrl());
		}
		
		// Load patient details
		Patient patient = patientRepository.findById(appointment.getPatientId()).orElse(null);
		if (patient != null) {
			dto.setPatientName(patient.getFullName());
		}
		
		return dto;
	}
}

