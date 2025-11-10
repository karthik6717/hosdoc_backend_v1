CREATE TABLE IF NOT EXISTS doctor_slot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    date DATE NOT NULL,
    slot_time TIME NOT NULL,
    duration_minutes INT DEFAULT 30,
    status ENUM('AVAILABLE','BOOKED','BLOCKED') NOT NULL DEFAULT 'AVAILABLE',
    appointment_id BIGINT NULL,
    UNIQUE KEY uk_doctor_date_slot (doctor_id, date, slot_time),
    FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE,
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_date (date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

