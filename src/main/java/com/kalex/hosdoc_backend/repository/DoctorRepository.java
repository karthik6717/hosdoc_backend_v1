package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	Optional<Doctor> findByUserId(Integer userId);
	
	@Query("SELECT DISTINCT d FROM Doctor d " +
			"JOIN DoctorSpecialty ds ON d.id = ds.doctorId " +
			"WHERE ds.specialtyId = :specialtyId")
	List<Doctor> findBySpecialtyId(@Param("specialtyId") Long specialtyId);
	
	@Query("SELECT DISTINCT d FROM Doctor d " +
			"LEFT JOIN DoctorSpecialty ds ON d.id = ds.doctorId " +
			"WHERE (:specialtyId IS NULL OR ds.specialtyId = :specialtyId) " +
			"AND (:name IS NULL OR LOWER(d.displayName) LIKE LOWER(CONCAT('%', :name, '%')))")
	List<Doctor> findWithFilters(@Param("specialtyId") Long specialtyId, @Param("name") String name);
}

