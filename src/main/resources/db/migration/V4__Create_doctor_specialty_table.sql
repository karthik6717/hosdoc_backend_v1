CREATE TABLE IF NOT EXISTS doctor_specialty (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    specialty_id BIGINT NOT NULL,
    UNIQUE KEY uk_doctor_specialty (doctor_id, specialty_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES specialty(id) ON DELETE CASCADE,
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_specialty_id (specialty_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

