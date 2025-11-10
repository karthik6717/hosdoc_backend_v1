package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.DoctorSlot;
import com.kalex.hosdoc_backend.model.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Long> {
	List<DoctorSlot> findByDoctorIdAndDate(Long doctorId, LocalDate date);
	List<DoctorSlot> findByDoctorIdAndDateAndStatus(Long doctorId, LocalDate date, SlotStatus status);
	Optional<DoctorSlot> findByDoctorIdAndDateAndSlotTime(Long doctorId, LocalDate date, LocalTime slotTime);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT s FROM DoctorSlot s WHERE s.doctorId = :doctorId AND s.date = :date AND s.slotTime = :slotTime AND s.status = 'AVAILABLE'")
	Optional<DoctorSlot> findAvailableSlotForBooking(@Param("doctorId") Long doctorId, 
													  @Param("date") LocalDate date, 
													  @Param("slotTime") LocalTime slotTime);
	
	@Modifying
	@Query("UPDATE DoctorSlot s SET s.status = :status, s.appointmentId = :appointmentId WHERE s.id = :slotId")
	int updateSlotStatus(@Param("slotId") Long slotId, @Param("status") SlotStatus status, @Param("appointmentId") Long appointmentId);
}

