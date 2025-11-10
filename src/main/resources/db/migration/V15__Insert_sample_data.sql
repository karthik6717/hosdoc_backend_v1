-- Sample Data for HosDoc Backend
-- This migration inserts sample data for testing

-- Note: User IDs (1, 2, 3) should exist in hosdoc_auth_user table
-- If they don't exist, create them first in the auth service

-- Insert sample specialties (if not already inserted)
INSERT INTO specialty (code, name, description, icon_url) VALUES
('CARD', 'Cardiology', 'Heart related care', NULL),
('NEUR', 'Neurology', 'Brain and nervous system', NULL),
('ORTH', 'Orthopedics', 'Bones and joints', NULL),
('PEDI', 'Pediatrics', 'Child healthcare', NULL),
('DERM', 'Dermatology', 'Skin care', NULL)
ON DUPLICATE KEY UPDATE name=name;

-- Insert languages (if not already inserted)
INSERT INTO language_master (code, name) VALUES
('en', 'English'),
('hi', 'Hindi'),
('te', 'Telugu'),
('ta', 'Tamil'),
('kn', 'Kannada')
ON DUPLICATE KEY UPDATE name=name;

-- Insert sample doctors
-- Note: user_id should reference existing users in hosdoc_auth_user table
INSERT INTO doctor (user_id, display_name, slug, about, fee, years_of_experience, rating, profile_image_url) VALUES
(1, 'Dr. Rajesh Kumar', 'dr-rajesh-kumar', 'Senior Cardiologist with 15 years of experience in treating heart diseases', 500.00, 15, 4.8, 'https://example.com/doctors/dr-rajesh.jpg'),
(2, 'Dr. Priya Sharma', 'dr-priya-sharma', 'Expert Neurologist specializing in brain disorders and stroke treatment', 600.00, 12, 4.7, 'https://example.com/doctors/dr-priya.jpg'),
(3, 'Dr. Amit Patel', 'dr-amit-patel', 'Orthopedic surgeon with expertise in joint replacement surgeries', 550.00, 10, 4.6, 'https://example.com/doctors/dr-amit.jpg')
ON DUPLICATE KEY UPDATE display_name=display_name;

-- Get doctor IDs (assuming they are 1, 2, 3)
SET @doctor1_id = (SELECT id FROM doctor WHERE user_id = 1 LIMIT 1);
SET @doctor2_id = (SELECT id FROM doctor WHERE user_id = 2 LIMIT 1);
SET @doctor3_id = (SELECT id FROM doctor WHERE user_id = 3 LIMIT 1);

-- Get specialty IDs
SET @card_id = (SELECT id FROM specialty WHERE code = 'CARD' LIMIT 1);
SET @neur_id = (SELECT id FROM specialty WHERE code = 'NEUR' LIMIT 1);
SET @orth_id = (SELECT id FROM specialty WHERE code = 'ORTH' LIMIT 1);

-- Link doctors to specialties
INSERT INTO doctor_specialty (doctor_id, specialty_id) VALUES
(@doctor1_id, @card_id),
(@doctor2_id, @neur_id),
(@doctor3_id, @orth_id)
ON DUPLICATE KEY UPDATE doctor_id=doctor_id;

-- Insert qualifications
INSERT INTO qualification (doctor_id, qualification, institution, year) VALUES
(@doctor1_id, 'MBBS', 'AIIMS Delhi', 2008),
(@doctor1_id, 'MD Cardiology', 'AIIMS Delhi', 2012),
(@doctor2_id, 'MBBS', 'PGI Chandigarh', 2010),
(@doctor2_id, 'DM Neurology', 'NIMHANS Bangalore', 2015),
(@doctor3_id, 'MBBS', 'CMC Vellore', 2012),
(@doctor3_id, 'MS Orthopedics', 'CMC Vellore', 2016)
ON DUPLICATE KEY UPDATE qualification=qualification;

-- Get language IDs
SET @en_id = (SELECT id FROM language_master WHERE code = 'en' LIMIT 1);
SET @hi_id = (SELECT id FROM language_master WHERE code = 'hi' LIMIT 1);
SET @te_id = (SELECT id FROM language_master WHERE code = 'te' LIMIT 1);

-- Link doctors to languages
INSERT INTO doctor_language (doctor_id, language_id) VALUES
(@doctor1_id, @en_id),
(@doctor1_id, @hi_id),
(@doctor2_id, @en_id),
(@doctor2_id, @hi_id),
(@doctor2_id, @te_id),
(@doctor3_id, @en_id),
(@doctor3_id, @hi_id)
ON DUPLICATE KEY UPDATE doctor_id=doctor_id;

-- Insert doctor availability (for next 7 days)
-- Doctor 1 - Morning shift
INSERT INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active) VALUES
(@doctor1_id, CURDATE(), 'MORNING', '09:00:00', '12:00:00', 30, TRUE),
(@doctor1_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'MORNING', '09:00:00', '12:00:00', 30, TRUE),
(@doctor1_id, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'MORNING', '09:00:00', '12:00:00', 30, TRUE),
(@doctor1_id, CURDATE(), 'AFTERNOON', '14:00:00', '17:00:00', 30, TRUE),
(@doctor1_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'AFTERNOON', '14:00:00', '17:00:00', 30, TRUE)
ON DUPLICATE KEY UPDATE is_active=TRUE;

-- Doctor 2 - Morning and Evening
INSERT INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active) VALUES
(@doctor2_id, CURDATE(), 'MORNING', '10:00:00', '13:00:00', 30, TRUE),
(@doctor2_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'MORNING', '10:00:00', '13:00:00', 30, TRUE),
(@doctor2_id, CURDATE(), 'EVENING', '17:00:00', '20:00:00', 30, TRUE),
(@doctor2_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'EVENING', '17:00:00', '20:00:00', 30, TRUE)
ON DUPLICATE KEY UPDATE is_active=TRUE;

-- Doctor 3 - Afternoon
INSERT INTO doctor_availability (doctor_id, date, shift, start_time, end_time, slot_duration_minutes, is_active) VALUES
(@doctor3_id, CURDATE(), 'AFTERNOON', '13:00:00', '16:00:00', 30, TRUE),
(@doctor3_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'AFTERNOON', '13:00:00', '16:00:00', 30, TRUE),
(@doctor3_id, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'AFTERNOON', '13:00:00', '16:00:00', 30, TRUE)
ON DUPLICATE KEY UPDATE is_active=TRUE;

-- Generate slots for doctor availability
-- This is a simplified version - in production, you might want to generate slots programmatically
-- For now, we'll insert a few sample slots

-- Doctor 1 slots for today morning
INSERT INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status) VALUES
(@doctor1_id, CURDATE(), '09:00:00', 30, 'AVAILABLE'),
(@doctor1_id, CURDATE(), '09:30:00', 30, 'AVAILABLE'),
(@doctor1_id, CURDATE(), '10:00:00', 30, 'AVAILABLE'),
(@doctor1_id, CURDATE(), '10:30:00', 30, 'AVAILABLE'),
(@doctor1_id, CURDATE(), '11:00:00', 30, 'AVAILABLE'),
(@doctor1_id, CURDATE(), '11:30:00', 30, 'AVAILABLE')
ON DUPLICATE KEY UPDATE status=status;

-- Doctor 2 slots for today morning
INSERT INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status) VALUES
(@doctor2_id, CURDATE(), '10:00:00', 30, 'AVAILABLE'),
(@doctor2_id, CURDATE(), '10:30:00', 30, 'AVAILABLE'),
(@doctor2_id, CURDATE(), '11:00:00', 30, 'AVAILABLE'),
(@doctor2_id, CURDATE(), '11:30:00', 30, 'AVAILABLE'),
(@doctor2_id, CURDATE(), '12:00:00', 30, 'AVAILABLE'),
(@doctor2_id, CURDATE(), '12:30:00', 30, 'AVAILABLE')
ON DUPLICATE KEY UPDATE status=status;

-- Doctor 3 slots for today afternoon
INSERT INTO doctor_slot (doctor_id, date, slot_time, duration_minutes, status) VALUES
(@doctor3_id, CURDATE(), '13:00:00', 30, 'AVAILABLE'),
(@doctor3_id, CURDATE(), '13:30:00', 30, 'AVAILABLE'),
(@doctor3_id, CURDATE(), '14:00:00', 30, 'AVAILABLE'),
(@doctor3_id, CURDATE(), '14:30:00', 30, 'AVAILABLE'),
(@doctor3_id, CURDATE(), '15:00:00', 30, 'AVAILABLE'),
(@doctor3_id, CURDATE(), '15:30:00', 30, 'AVAILABLE')
ON DUPLICATE KEY UPDATE status=status;

-- Note: Patient records should be created via API after user registration
-- Sample patient data (assuming user_id 4, 5 exist in auth service)
-- INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details) VALUES
-- (4, 'Karthik G', 'MALE', '1990-01-15', 'SELF', '9876543210', TRUE),
-- (5, 'Priya K', 'FEMALE', '1992-05-20', 'SELF', '9876543211', TRUE);

