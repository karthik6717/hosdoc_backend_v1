CREATE TABLE IF NOT EXISTS report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_user_id INT NOT NULL,
    patient_id BIGINT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    file_url VARCHAR(2048) NOT NULL,
    storage_provider ENUM('LOCAL','S3','GCS') DEFAULT 'LOCAL',
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_by INT NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE SET NULL,
    INDEX idx_patient_user_id (patient_user_id),
    INDEX idx_patient_id (patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

