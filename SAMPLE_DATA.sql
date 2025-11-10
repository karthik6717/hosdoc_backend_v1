-- Sample Data for HosDoc Backend
-- Run this script manually after the database is set up
-- Make sure users exist in hosdoc_auth_user table first

USE karthik;

-- Insert specialties (if not exists)
INSERT IGNORE INTO specialty (code, name, description, icon_url, created_at) VALUES
('CARD', 'Cardiology', 'Heart related care', NULL, NOW()),
('NEUR', 'Neurology', 'Brain and nervous system', NULL, NOW()),
('ORTH', 'Orthopedics', 'Bones and joints', NULL, NOW()),
('PEDI', 'Pediatrics', 'Child healthcare', NULL, NOW()),
('DERM', 'Dermatology', 'Skin care', NULL, NOW());

-- Insert languages (if not exists)
INSERT IGNORE INTO language_master (code, name) VALUES
('en', 'English'),
('hi', 'Hindi'),
('te', 'Telugu'),
('ta', 'Tamil'),
('kn', 'Kannada');

-- Insert sample doctors
-- Note: user_id should reference existing users in hosdoc_auth_user table
-- Assuming users with IDs 1, 2, 3 exist in auth service
INSERT IGNORE INTO doctor (user_id, display_name, slug, about, fee, years_of_experience, rating, profile_image_url) VALUES
(1, 'Dr. Rajesh Kumar', 'dr-rajesh-kumar', 'Senior Cardiologist with 15 years of experience in treating heart diseases', 500.00, 15, 4.8, 'https://example.com/doctors/dr-rajesh.jpg'),
(2, 'Dr. Priya Sharma', 'dr-priya-sharma', 'Expert Neurologist specializing in brain disorders and stroke treatment', 600.00, 12, 4.7, 'https://example.com/doctors/dr-priya.jpg'),
(3, 'Dr. Amit Patel', 'dr-amit-patel', 'Orthopedic surgeon with expertise in joint replacement surgeries', 550.00, 10, 4.6, 'https://example.com/doctors/dr-amit.jpg');

-- Link doctors to specialties
-- Get IDs dynamically
INSERT IGNORE INTO doctor_specialty (doctor_id, specialty_id)
SELECT d.id, s.id 
FROM doctor d, specialty s 
WHERE d.user_id = 1 AND s.code = 'CARD';

INSERT IGNORE INTO doctor_specialty (doctor_id, specialty_id)
SELECT d.id, s.id 
FROM doctor d, specialty s 
WHERE d.user_id = 2 AND s.code = 'NEUR';

INSERT IGNORE INTO doctor_specialty (doctor_id, specialty_id)
SELECT d.id, s.id 
FROM doctor d, specialty s 
WHERE d.user_id = 3 AND s.code = 'ORTH';

-- Insert qualifications
INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'MBBS', 'AIIMS Delhi', 2008
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'MD Cardiology', 'AIIMS Delhi', 2012
FROM doctor d WHERE d.user_id = 1;

INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'MBBS', 'PGI Chandigarh', 2010
FROM doctor d WHERE d.user_id = 2;

INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'DM Neurology', 'NIMHANS Bangalore', 2015
FROM doctor d WHERE d.user_id = 2;

INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'MBBS', 'CMC Vellore', 2012
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO qualification (doctor_id, qualification, institution, year)
SELECT d.id, 'MS Orthopedics', 'CMC Vellore', 2016
FROM doctor d WHERE d.user_id = 3;

-- Link doctors to languages
INSERT IGNORE INTO doctor_language (doctor_id, language_id)
SELECT d.id, l.id 
FROM doctor d, language_master l 
WHERE d.user_id = 1 AND l.code IN ('en', 'hi');

INSERT IGNORE INTO doctor_language (doctor_id, language_id)
SELECT d.id, l.id 
FROM doctor d, language_master l 
WHERE d.user_id = 2 AND l.code IN ('en', 'hi', 'te');

INSERT IGNORE INTO doctor_language (doctor_id, language_id)
SELECT d.id, l.id 
FROM doctor d, language_master l 
WHERE d.user_id = 3 AND l.code IN ('en', 'hi');

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
FROM doctor d WHERE d.user_id = 2;

INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'EVENING', '17:00:00', '20:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 2;

INSERT IGNORE INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active)
SELECT d.id, CURDATE(), 'AFTERNOON', '13:00:00', '16:00:00', 30, TRUE
FROM doctor d WHERE d.user_id = 3;

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
FROM doctor d WHERE d.user_id = 2;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '10:30:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 2;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '11:00:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 2;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '13:00:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '13:30:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 3;

INSERT IGNORE INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status)
SELECT d.id, CURDATE(), '14:00:00', 30, 'AVAILABLE'
FROM doctor d WHERE d.user_id = 3;

-- Insert patient records based on hosdoc_auth_user table
-- These correspond to users with role 'PATIENT' in hosdoc_auth_user
-- patient.user_id references hosdoc_auth_user.id

-- User id 5: raj@gmail.com
INSERT IGNORE INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (5, 'Raj Kumar', 'MALE', '1990-05-15', 'SELF', '9876543210', TRUE, NOW(), NOW());

-- User id 6: ramu@gmail.com
INSERT IGNORE INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (6, 'Ramu Reddy', 'MALE', '1988-03-20', 'SELF', '9876543211', TRUE, NOW(), NOW());

-- User id 7: rishi@gmail.com
INSERT IGNORE INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (7, 'Rishi Sharma', 'MALE', '1992-07-10', 'SELF', '9876543212', TRUE, NOW(), NOW());

