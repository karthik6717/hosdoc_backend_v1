package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.Appointment;
import com.kalex.hosdoc_backend.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	Optional<Appointment> findByAppointmentUid(String appointmentUid);
	List<Appointment> findByBookedByUserId(Integer userId);
	List<Appointment> findByBookedByUserIdAndStatus(Integer userId, AppointmentStatus status);
	List<Appointment> findByDoctorId(Long doctorId);
	List<Appointment> findByPatientId(Long patientId);
	
	@Query("SELECT a FROM Appointment a WHERE a.bookedByUserId = :userId AND (:status IS NULL OR a.status = :status) ORDER BY a.date DESC, a.slotTime DESC")
	List<Appointment> findByBookedByUserIdWithStatus(@Param("userId") Integer userId, @Param("status") AppointmentStatus status);
	
	@Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctorId = :doctorId AND a.date = :date AND a.status IN ('CONFIRMED', 'COMPLETED')")
	long countConfirmedAppointmentsForDoctorOnDate(@Param("doctorId") Long doctorId, @Param("date") java.time.LocalDate date);
}

