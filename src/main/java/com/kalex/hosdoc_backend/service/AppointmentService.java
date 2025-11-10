package com.kalex.hosdoc_backend.service;

import com.kalex.hosdoc_backend.dto.AppointmentBookingRequestDTO;
import com.kalex.hosdoc_backend.dto.AppointmentConfirmationDTO;
import com.kalex.hosdoc_backend.dto.AppointmentDTO;
import com.kalex.hosdoc_backend.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {
	AppointmentConfirmationDTO bookAppointment(Integer userId, AppointmentBookingRequestDTO request);
	AppointmentDTO getAppointmentByUid(String appointmentUid);
	List<AppointmentDTO> getAppointmentsByUserId(Integer userId, AppointmentStatus status);
	AppointmentDTO cancelAppointment(Integer userId, String appointmentUid);
	AppointmentDTO rescheduleAppointment(Integer userId, String appointmentUid, AppointmentBookingRequestDTO newRequest);
}

