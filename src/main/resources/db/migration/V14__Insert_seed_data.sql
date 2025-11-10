-- Insert specialties
INSERT INTO specialty (code, name, description, icon_url) VALUES
('CARD', 'Cardiology', 'Heart related care', NULL),
('NEUR', 'Neurology', 'Brain and nervous system', NULL),
('ORTH', 'Orthopedics', 'Bones and joints', NULL),
('PEDI', 'Pediatrics', 'Child healthcare', NULL),
('DERM', 'Dermatology', 'Skin care', NULL)
ON DUPLICATE KEY UPDATE name=name;

-- Insert languages
INSERT INTO language_master (code, name) VALUES
('en', 'English'),
('hi', 'Hindi'),
('te', 'Telugu'),
('ta', 'Tamil'),
('kn', 'Kannada')
ON DUPLICATE KEY UPDATE name=name;

-- Note: Doctor, Patient, and other user-dependent data should be inserted via application
-- or separate seed scripts after user creation in auth service

