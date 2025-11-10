package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
	List<Report> findByPatientUserId(Integer patientUserId);
	List<Report> findByPatientId(Long patientId);
}

