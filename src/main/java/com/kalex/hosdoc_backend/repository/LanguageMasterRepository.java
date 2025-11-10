package com.kalex.hosdoc_backend.repository;

import com.kalex.hosdoc_backend.entity.LanguageMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageMasterRepository extends JpaRepository<LanguageMaster, Long> {
	Optional<LanguageMaster> findByCode(String code);
}

