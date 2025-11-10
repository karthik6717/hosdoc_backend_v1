-- Example: Complete Family Member Setup for User ID 5 (raj@gmail.com)
-- This demonstrates how multiple relations are stored for one user

USE karthik;

-- User 5 (raj@gmail.com) - Add SELF record (if not exists)
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (5, 'Raj Kumar', 'MALE', '1990-05-15', 'SELF', '9876543210', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- User 5 - Add SPOUSE
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (5, 'Priya Kumar', 'FEMALE', '1992-03-20', 'SPOUSE', '9876543211', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- User 5 - Add CHILD
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (5, 'Arjun Kumar', 'MALE', '2015-07-10', 'CHILD', NULL, TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- User 5 - Add PARENT
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (5, 'Ramesh Kumar', 'MALE', '1965-01-15', 'PARENT', '9876543213', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- User 6 (ramu@gmail.com) - Add SELF
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (6, 'Ramu Reddy', 'MALE', '1988-03-20', 'SELF', '9876543214', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- User 6 - Add SPOUSE
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (6, 'Lakshmi Reddy', 'FEMALE', '1990-08-25', 'SPOUSE', '9876543215', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- User 7 (rishi@gmail.com) - Add SELF
INSERT INTO patient (user_id, full_name, gender, dob, relation, phone, ack_details, created_at, updated_at) 
VALUES (7, 'Rishi Sharma', 'MALE', '1992-07-10', 'SELF', '9876543216', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE full_name=full_name;

-- View all relations for user 5
SELECT 
    p.id,
    p.user_id,
    p.full_name,
    p.relation,
    p.gender,
    p.dob,
    TIMESTAMPDIFF(YEAR, p.dob, CURDATE()) as age,
    p.phone
FROM patient p
WHERE p.user_id = 5
ORDER BY 
    CASE p.relation
        WHEN 'SELF' THEN 1
        WHEN 'SPOUSE' THEN 2
        WHEN 'CHILD' THEN 3
        WHEN 'PARENT' THEN 4
        ELSE 5
    END;

-- Summary: Count members by relation for all users
SELECT 
    p.user_id,
    p.relation,
    COUNT(*) as member_count,
    GROUP_CONCAT(p.full_name) as member_names
FROM patient p
GROUP BY p.user_id, p.relation
ORDER BY p.user_id, p.relation;

