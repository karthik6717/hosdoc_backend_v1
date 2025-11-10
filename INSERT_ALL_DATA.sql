-- Complete Data Insertion Script for HosDoc Backend
-- This script inserts all sample data based on existing hosdoc_auth_user records
-- Run this after ensuring specialties and languages are inserted

USE karthik;

-- ============================================
-- 1. INSERT DOCTORS (based on DOCTOR role users)
-- ============================================

-- User id 1: gkarthik@gmail.com -> Dr. Karthik G (Cardiology)
INSERT INTO doctor (user_id, display_name, slug, about, fee, years_of_experience, rating, profile_image_url, created_at, updated_at) 
VALUES (1, 'Dr. Karthik G', 'dr-karthik-g', 'Senior Cardiologist with extensive experience in heart care', 500.00, 15, 4.8, 'https://example.com/doctors/dr-karthik.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE display_name=display_name;

-- User id 3: vijay@gmail.com -> Dr. Vijay Kumar (Neurology)
INSERT INTO doctor (user_id, display_name, slug, about, fee, years_of_experience, rating, profile_image_url, created_at, updated_at) 
VALUES (3, 'Dr. Vijay Kumar', 'dr-vijay-kumar', 'Expert Neurologist specializing in brain disorders', 600.00, 12, 4.7, 'https://example.com/doctors/dr-vijay.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE display_name=display_name;

-- User id 4: alex@gmail.com -> Dr. Alex Johnson (Orthopedics)
INSERT INTO doctor (user_id, display_name, slug, about, fee, years_of_experience, rating, profile_image_url, created_at, updated_at) 
VALUES (4, 'Dr. Alex Johnson', 'dr-alex-johnson', 'Orthopedic surgeon with expertise in joint replacement', 550.00, 10, 4.6, 'https://example.com/doctors/dr-alex.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE display_name=display_name;

-- ============================================
-- 2. LINK DOCTORS TO SPECIALTIES
-- ============================================

INSERT IGNORE INTO doctor_specialty (doctor_id, specialty_id)
SELECT d.id, s.id 
FROM doctor d, specialty s 
WHERE d.user_id = 1 AND s.code = 'CARD';

INSERT IGNORE INTO doctor_specialty (doctor_id, specialty_id)
SELECT d.id, s.id 
FROM doctor d, specialty s 
WHERE d.user_id = 3 AND s.code = 'NEUR';

INSERT IGNORE INTO doctor_specialty (doctor_id, specialty_id)
SELECT d.id, s.id 
FROM doctor d, specialty s 
WHERE d.user_id = 4 AND s.code = 'ORTH';

-- ============================================
-- 3. INSERT QUALIFICATIONS
-- ============================================

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

-- ============================================
-- 4. LINK DOCTORS TO LANGUAGES
-- ============================================

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

-- ============================================
-- 5. INSERT DOCTOR AVAILABILITY
-- ============================================

-- Dr. Karthik (user_id 1) - Morning and Afternoon
INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'MORNING', '09:00:00', '12:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'MORNING', '09:00:00', '12:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'AFTERNOON', '14:00:00', '17:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 1;

-- Dr. Vijay (user_id 3) - Morning and Evening
INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'MORNING', '10:00:00', '13:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'EVENING', '17:00:00', '20:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 3;

-- Dr. Alex (user_id 4) - Afternoon
INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'AFTERNOON', '13:00:00', '16:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 4;

-- ============================================
-- 6. GENERATE DOCTOR SLOTS
-- ============================================

-- Dr. Karthik slots (Morning 9:00-12:00)
INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), slot_time, 30, 'AVAILABLE'
FROM doctor d, (
    SELECT '09:00:00' as slot_time UNION SELECT '09:30:00' UNION SELECT '10:00:00' 
    UNION SELECT '10:30:00' UNION SELECT '11:00:00' UNION SELECT '11:30:00'
) slots
WHERE d.user_id = 1;

-- Dr. Vijay slots (Morning 10:00-13:00)
INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), slot_time, 30, 'AVAILABLE'
FROM doctor d, (
    SELECT '10:00:00' as slot_time UNION SELECT '10:30:00' UNION SELECT '11:00:00' 
    UNION SELECT '11:30:00' UNION SELECT '12:00:00' UNION SELECT '12:30:00'
) slots
WHERE d.user_id = 3;

-- Dr. Alex slots (Afternoon 13:00-16:00)
INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), slot_time, 30, 'AVAILABLE'
FROM doctor d, (
    SELECT '13:00:00' as slot_time UNION SELECT '13:30:00' UNION SELECT '14:00:00' 
    UNION SELECT '14:30:00' UNION SELECT '15:00:00' UNION SELECT '15:30:00'
) slots
WHERE d.user_id = 4;

-- ============================================
-- 7. INSERT PATIENTS (based on PATIENT role users)
-- ============================================

-- User id 5: raj@gmail.com
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (5, 'Raj Kumar', 'MALE', '1990-05-15', 'SELF', '9876543210', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- User id 6: ramu@gmail.com
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (6, 'Ramu Reddy', 'MALE', '1988-03-20', 'SELF', '9876543211', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- User id 7: rishi@gmail.com
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (7, 'Rishi Sharma', 'MALE', '1992-07-10', 'SELF', '9876543212', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

-- Verify doctors
SELECT '=== DOCTORS ===' as info;
SELECT d.id, d.user_id, d.display_name, d.fee, d.years_of_experience
FROM doctor d
ORDER BY d.id;

-- Verify patients
SELECT '=== PATIENTS ===' as info;
SELECT p.id, p.user_id, p.full_name, p.gender, p.dob, p.relation, p.phone
FROM patient p
ORDER BY p.id;

-- Verify doctor specialties
SELECT '=== DOCTOR SPECIALTIES ===' as info;
SELECT d.display_name, s.name as specialty
FROM doctor d
JOIN doctor_specialty ds ON d.id = ds.doctor_id
JOIN specialty s ON ds.specialty_id = s.id
ORDER BY d.id;

-- Verify availability
SELECT '=== DOCTOR AVAILABILITY ===' as info;
SELECT d.display_name, da.date, da.shift, da.start_time, da.end_time
FROM doctor d
JOIN doctor_availability da ON d.id = da.doctor_id
WHERE da.date >= CURDATE()
ORDER BY d.id, da.date, da.shift;

-- Verify slots
SELECT '=== AVAILABLE SLOTS ===' as info;
SELECT d.display_name, ds.date, ds.slot_time, ds.status
FROM doctor d
JOIN doctor_slot ds ON d.id = ds.doctor_id
WHERE ds.date = CURDATE() AND ds.status = 'AVAILABLE'
ORDER BY d.id, ds.slot_time;

