package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	List<AuditLog> findByUserId(Integer userId);
	List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);
}

