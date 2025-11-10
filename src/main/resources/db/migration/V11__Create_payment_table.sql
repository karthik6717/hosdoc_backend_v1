CREATE TABLE IF NOT EXISTS payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'INR',
    provider VARCHAR(100),
    provider_payment_id VARCHAR(255),
    status ENUM('INIT','SUCCESS','FAILED') NOT NULL DEFAULT 'INIT',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointment(id) ON DELETE RESTRICT,
    INDEX idx_appointment_id (appointment_id),
    INDEX idx_provider_payment_id (provider_payment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

