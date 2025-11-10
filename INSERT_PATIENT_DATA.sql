-- Insert Patient Data based on hosdoc_auth_user table
-- This script inserts patient records for users with role 'PATIENT'
-- The patient.user_id references hosdoc_auth_user.id

USE karthik;

-- Insert patient records for users with PATIENT role
-- User id 5: raj@gmail.com (userId: 4)
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (5, 'Raj Kumar', 'MALE', '1990-05-15', 'SELF', '9876543210', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- User id 6: ramu@gmail.com (userId: 5)
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (6, 'Ramu Reddy', 'MALE', '1988-03-20', 'SELF', '9876543211', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- User id 7: rishi@gmail.com (userId: 6)
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (7, 'Rishi Sharma', 'MALE', '1992-07-10', 'SELF', '9876543212', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- Verify inserted data
SELECT p.id, p.user_id, p.full_name, p.gender, p.dob, p.relation, p.phone, p.ack_details
FROM patient p
ORDER BY p.id;

