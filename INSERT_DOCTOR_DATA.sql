-- Insert Doctor Data based on hosdoc_auth_user table
-- This script inserts doctor records for users with role 'DOCTOR'
-- The doctor.user_id references hosdoc_auth_user.id

USE karthik;

-- Insert doctor records for users with DOCTOR role
-- User id 1: gkarthik@gmail.com (userId: 1)
INSERT INTO doctor (user_id, display_name, slug, about, fee, years_of_experience, rating, profile_image_url, created_at, updated_at) 
VALUES (1, 'Dr. Karthik G', 'dr-karthik-g', 'Senior Cardiologist with extensive experience in heart care', 500.00, 15, 4.8, 'https://example.com/doctors/dr-karthik.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE display_name=display_name;

-- User id 3: vijay@gmail.com (userId: 2)
INSERT INTO doctor (user_id, display_name, slug, about, fee, years_of_experience, rating, profile_image_url, created_at, updated_at) 
VALUES (3, 'Dr. Vijay Kumar', 'dr-vijay-kumar', 'Expert Neurologist specializing in brain disorders', 600.00, 12, 4.7, 'https://example.com/doctors/dr-vijay.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE display_name=display_name;

-- User id 4: alex@gmail.com (userId: 3)
INSERT INTO doctor (user_id, display_name, slug, about, fee, years_of_experience, rating, profile_image_url, created_at, updated_at) 
VALUES (4, 'Dr. Alex Johnson', 'dr-alex-johnson', 'Orthopedic surgeon with expertise in joint replacement', 550.00, 10, 4.6, 'https://example.com/doctors/dr-alex.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE display_name=display_name;

-- Link doctors to specialties
-- Dr. Karthik (user_id 1) -> Cardiology
INSERT IGNORE INTO doctor_specialty (doctor_id, specialty_id)
SELECT d.id, s.id 
FROM doctor d, specialty s 
WHERE d.user_id = 1 AND s.code = 'CARD';

-- Dr. Vijay (user_id 3) -> Neurology
INSERT IGNORE INTO doctor_specialty (doctor_id, specialty_id)
SELECT d.id, s.id 
FROM doctor d, specialty s 
WHERE d.user_id = 3 AND s.code = 'NEUR';

-- Dr. Alex (user_id 4) -> Orthopedics
INSERT IGNORE INTO doctor_specialty (doctor_id, specialty_id)
SELECT d.id, s.id 
FROM doctor d, specialty s 
WHERE d.user_id = 4 AND s.code = 'ORTH';

-- Insert qualifications
INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'MBBS', 'AIIMS Delhi', 2008
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'MD Cardiology', 'AIIMS Delhi', 2012
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'MBBS', 'PGI Chandigarh', 2010
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'DM Neurology', 'NIMHANS Bangalore', 2015
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'MBBS', 'CMC Vellore', 2012
FROM doctor d WHERE d.user_id = 4;

INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'MS Orthopedics', 'CMC Vellore', 2016
FROM doctor d WHERE d.user_id = 4;

-- Link doctors to languages
INSERT IGNORE INTO doctor_language (doctor_id, language_id)
SELECT d.id, l.id 
FROM doctor d, language_master l 
WHERE d.user_id = 1 AND l.code IN ('en', 'hi');

INSERT IGNORE INTO doctor_language (doctor_id, language_id)
SELECT d.id, l.id 
FROM doctor d, language_master l 
WHERE d.user_id = 3 AND l.code IN ('en', 'hi', 'te');

INSERT IGNORE INTO doctor_language (doctor_id, language_id)
SELECT d.id, l.id 
FROM doctor d, language_master l 
WHERE d.user_id = 4 AND l.code IN ('en', 'hi');

-- Insert doctor availability (for next 7 days)
INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'MORNING', '09:00:00', '12:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'MORNING', '09:00:00', '12:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'AFTERNOON', '14:00:00', '17:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'MORNING', '10:00:00', '13:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'EVENING', '17:00:00', '20:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'AFTERNOON', '13:00:00', '16:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 4;

-- Generate sample slots for today
INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '09:00:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '09:30:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '10:00:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '10:30:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '11:00:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '11:30:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '10:00:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '10:30:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '11:00:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '13:00:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 4;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '13:30:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 4;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '14:00:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 4;

-- Verify inserted data
SELECT d.id, d.user_id, d.display_name, d.fee, d.years_of_experience
FROM doctor d
ORDER BY d.id;

