-- DML Script for MosqueLink Database (PostgreSQL)
-- This script inserts sample data into the mosques, announcements, and events tables.
-- Run this script manually after creating the tables with ddl.sql.

-- Set a convenient date for upcoming events/announcements relative to now
-- Adjust this base date if you want events/announcements to be in the past or far future
-- For current testing, CURRENT_TIMESTAMP and CURRENT_DATE are used which are dynamic.

-- Insert Mosques (these are the fixed three)
INSERT INTO mosques (name, address, city, state, zip_code, contact_email, phone_number, description, logo_url, created_at, updated_at)
VALUES ('Darul-islah', '446 Central Ave', 'New Providence', 'NJ', '07974', 'info@darulislah.org', '908-665-2700', 'Darul Islah is a community mosque dedicated to serving the needs of the local Muslim population.', '/assets/logos/darul-islah.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO mosques (name, address, city, state, zip_code, contact_email, phone_number, description, logo_url, created_at, updated_at)
VALUES ('ICPC', '1060 Pompton Ave', 'Cedar Grove', 'NJ', '07009', 'info@icpc.org', '973-857-4141', 'Islamic Center of Passaic County, a focal point for spiritual and community development.', '/assets/logos/icpc.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO mosques (name, address, city, state, zip_code, contact_email, phone_number, description, logo_url, created_at, updated_at)
VALUES ('MCGP', '1440 State Rd', 'Princeton', 'NJ', '08540', 'info@mcgp.org', '609-683-0900', 'Muslim Center of Greater Princeton, fostering a strong and inclusive community.', '/assets/logos/mcgp.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- Insert Sample Announcements (using subqueries to link to mosque IDs)
INSERT INTO announcements (mosque_id, title, content, publish_date, expiration_date, is_published, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'Darul-islah'),
           'Eid ul-Adha Prayer & Celebration',
           'Join us for Eid ul-Adha prayers followed by a community potluck breakfast. Eid Mubarak! Please check our website for details.',
           CURRENT_TIMESTAMP - INTERVAL '2 hour', -- Published slightly in the past
           CURRENT_TIMESTAMP + INTERVAL '10 day', -- Expires in 10 days
           TRUE,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

INSERT INTO announcements (mosque_id, title, content, publish_date, expiration_date, is_published, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'ICPC'),
           'Weekly Tafseer & Potluck Dinner',
           'Every Friday after Maghrib, join us for Quranic tafseer followed by a community potluck dinner. All are welcome!',
           CURRENT_TIMESTAMP - INTERVAL '1 day',
           NULL, -- No expiration
           TRUE,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

INSERT INTO announcements (mosque_id, title, content, publish_date, expiration_date, is_published, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'MCGP'),
           'Youth Summer Camp Registration Open',
           'Our annual summer camp for youth aged 10-16 is now open for registration. Limited spots available!',
           CURRENT_TIMESTAMP - INTERVAL '5 day',
           CURRENT_TIMESTAMP + INTERVAL '30 day',
           TRUE,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

INSERT INTO announcements (mosque_id, title, content, publish_date, expiration_date, is_published, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'Darul-islah'),
           'Important: New COVID-19 Guidelines',
           'Kindly adhere to the updated health and safety guidelines when visiting the mosque. Masks are now optional.',
           CURRENT_TIMESTAMP - INTERVAL '10 day',
           CURRENT_TIMESTAMP + INTERVAL '60 day',
           TRUE,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

INSERT INTO announcements (mosque_id, title, content, publish_date, expiration_date, is_published, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'ICPC'),
           'Upcoming Lecture Series: The Life of the Prophet (PBUH)',
           'Join us every Saturday for an enlightening lecture series on the Seerah of Prophet Muhammad (PBUH). Starting July 5th.',
           CURRENT_TIMESTAMP - INTERVAL '3 day',
           NULL,
           TRUE,
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );


-- Insert Sample Events (using subqueries to link to mosque IDs)
INSERT INTO events (mosque_id, title, description, event_date, start_time, end_time, location, capacity, registration_required, status, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'Darul-islah'),
           'Weekly Kids Quran Class',
           'Interactive Quran learning session for children aged 5-10.',
           CURRENT_DATE + INTERVAL '7 day', -- Next week
           '17:00:00',
           '18:00:00',
           'Kids Classroom',
           25,
           TRUE,
           'SCHEDULED',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

INSERT INTO events (mosque_id, title, description, event_date, start_time, end_time, location, capacity, registration_required, status, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'ICPC'),
           'Sisters Book Club',
           'Monthly book club meeting for sisters. This month''s book: "Inner Dimensions of Islam".',
           CURRENT_DATE + INTERVAL '14 day', -- In two weeks
           '14:00:00',
           '15:30:00',
           'Sisters Hall',
           NULL, -- No capacity limit
           FALSE,
           'SCHEDULED',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

INSERT INTO events (mosque_id, title, description, event_date, start_time, end_time, location, capacity, registration_required, status, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'MCGP'),
           'Community Health Fair',
           'Free health screenings, wellness workshops, and advice from local healthcare professionals. All ages welcome!',
           CURRENT_DATE + INTERVAL '20 day', -- In three weeks
           '09:00:00',
           '17:00:00',
           'Gymnasium',
           200,
           TRUE,
           'SCHEDULED',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

INSERT INTO events (mosque_id, title, description, event_date, start_time, end_time, location, capacity, registration_required, status, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'Darul-islah'),
           'Adult Tajweed Course',
           'Advanced Tajweed course for adults. Learn proper Quranic recitation. Registration required.',
           CURRENT_DATE + INTERVAL '5 day',
           '19:00:00',
           '20:30:00',
           'Main Prayer Hall',
           50,
           TRUE,
           'SCHEDULED',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

INSERT INTO events (mosque_id, title, description, event_date, start_time, end_time, location, capacity, registration_required, status, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'ICPC'),
           'Youth Sports Day',
           'A day of fun and sports activities for children and youth. Come join us!',
           CURRENT_DATE + INTERVAL '12 day',
           '10:00:00',
           '14:00:00',
           'Outdoor Field',
           100,
           FALSE,
           'SCHEDULED',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );

-- Insert Sample Prayer Times (for today and tomorrow)
-- Darul-islah (ID 1) today
INSERT INTO prayer_times (mosque_id, date, fajr, dhuhr, asr, maghrib, isha, sunrise, juma, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'Darul-islah'),
           CURRENT_DATE,
           '04:30:00', '13:00:00', '17:00:00', '20:30:00', '22:00:00', '06:00:00', NULL,
           CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
       );
-- ICPC (ID 2) today
INSERT INTO prayer_times (mosque_id, date, fajr, dhuhr, asr, maghrib, isha, sunrise, juma, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'ICPC'),
           CURRENT_DATE,
           '04:35:00', '13:05:00', '17:05:00', '20:35:00', '22:05:00', '06:05:00', '13:30:00',
           CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
       );
-- MCGP (ID 3) today
INSERT INTO prayer_times (mosque_id, date, fajr, dhuhr, asr, maghrib, isha, sunrise, juma, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'MCGP'),
           CURRENT_DATE,
           '04:40:00', '13:10:00', '17:10:00', '20:40:00', '22:10:00', '06:10:00', NULL,
           CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
       );
-- Darul-islah (ID 1) tomorrow
INSERT INTO prayer_times (mosque_id, date, fajr, dhuhr, asr, maghrib, isha, sunrise, juma, created_at, updated_at)
VALUES (
           (SELECT id FROM mosques WHERE name = 'Darul-islah'),
           CURRENT_DATE + INTERVAL '1 day',
           '04:31:00', '13:01:00', '17:01:00', '20:29:00', '21:59:00', '06:01:00', NULL,
           CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
       );