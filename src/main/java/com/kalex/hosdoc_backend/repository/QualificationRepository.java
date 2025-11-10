package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long> {
	List<Qualification> findByDoctorId(Long doctorId);
}
