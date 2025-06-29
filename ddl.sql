-- DDL Script for MosqueLink Database (PostgreSQL)
-- This script creates tables for Mosques, Events, Announcements, and Prayer Times.
-- It explicitly excludes tables for Users, Memberships, Volunteer Applications, and Service Requests,
-- as these will be handled by external forms/services.
-- Foreign key for created_by_user_id is also removed from events and announcements.

-- Drop existing tables and types if they exist to ensure a clean slate
-- Order matters due to foreign key dependencies
DROP TABLE IF EXISTS prayer_times CASCADE;
DROP TABLE IF EXISTS announcements CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS mosques CASCADE;
DROP TABLE IF EXISTS event_registrations CASCADE;
DROP TABLE IF EXISTS memberships CASCADE;
DROP TABLE IF EXISTS service_requests CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS spring_session_attributes CASCADE;
DROP TABLE IF EXISTS spring_session CASCADE;
DROP TABLE IF EXISTS volunteer_applications CASCADE;
-- Drop custom ENUM types (only EVENT_STATUS remains relevant for backend persistence)
DROP TYPE IF EXISTS EVENT_STATUS;

-- Create Custom ENUM Type
CREATE TYPE EVENT_STATUS AS ENUM ('SCHEDULED', 'CANCELLED', 'COMPLETED');

-- Table: mosques
CREATE TABLE mosques (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) UNIQUE NOT NULL, -- Ensure mosque names are unique
                         address VARCHAR(255) NOT NULL,
                         city VARCHAR(100) NOT NULL,
                         state VARCHAR(100) NOT NULL,
                         zip_code VARCHAR(10) NOT NULL,
                         contact_email VARCHAR(255) NOT NULL,
                         phone_number VARCHAR(20) NOT NULL,
                         description TEXT,
                         logo_url VARCHAR(255), -- Stores URL/path for mosque logo image
                         created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Trigger function to update 'updated_at' timestamp for 'mosques' table
CREATE OR REPLACE FUNCTION update_mosques_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_mosques_updated_at
    BEFORE UPDATE ON mosques
    FOR EACH ROW
    EXECUTE FUNCTION update_mosques_updated_at();

-- Table: events
CREATE TABLE events (
                        id BIGSERIAL PRIMARY KEY,
                        mosque_id BIGINT NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        event_date DATE NOT NULL,
                        start_time TIME WITHOUT TIME ZONE NOT NULL,
                        end_time TIME WITHOUT TIME ZONE NOT NULL,
                        location VARCHAR(255),
                        capacity INTEGER,
                        registration_required BOOLEAN NOT NULL DEFAULT FALSE,
                        status EVENT_STATUS NOT NULL,
    -- created_by_user_id column has been removed as per simplified scope
                        created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                        FOREIGN KEY (mosque_id) REFERENCES mosques(id) ON DELETE CASCADE -- If mosque deleted, delete its events
);

-- Trigger function to update 'updated_at' timestamp for 'events' table
CREATE OR REPLACE FUNCTION update_events_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_events_updated_at
    BEFORE UPDATE ON events
    FOR EACH ROW
    EXECUTE FUNCTION update_events_updated_at();

-- Table: announcements
CREATE TABLE announcements (
                               id BIGSERIAL PRIMARY KEY,
                               mosque_id BIGINT NOT NULL,
                               title VARCHAR(255) NOT NULL,
                               content TEXT NOT NULL,
                               publish_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                               expiration_date TIMESTAMP WITHOUT TIME ZONE, -- Can be null
                               is_published BOOLEAN NOT NULL DEFAULT TRUE,
    -- created_by_user_id column has been removed as per simplified scope
                               created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               FOREIGN KEY (mosque_id) REFERENCES mosques(id) ON DELETE CASCADE -- If mosque deleted, delete its announcements
);

-- Trigger function to update 'updated_at' timestamp for 'announcements' table
CREATE OR REPLACE FUNCTION update_announcements_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_announcements_updated_at
    BEFORE UPDATE ON announcements
    FOR EACH ROW
    EXECUTE FUNCTION update_announcements_updated_at();

-- Table: prayer_times
CREATE TABLE prayer_times (
                              id BIGSERIAL PRIMARY KEY,
                              mosque_id BIGINT NOT NULL,
                              date DATE NOT NULL,
                              fajr TIME WITHOUT TIME ZONE NOT NULL,
                              dhuhr TIME WITHOUT TIME ZONE NOT NULL,
                              asr TIME WITHOUT TIME ZONE NOT NULL,
                              maghrib TIME WITHOUT TIME ZONE NOT NULL,
                              isha TIME WITHOUT TIME ZONE NOT NULL,
                              sunrise TIME WITHOUT TIME ZONE, -- Can be null
                              juma TIME WITHOUT TIME ZONE, -- Can be null, specific for Fridays
                              created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              FOREIGN KEY (mosque_id) REFERENCES mosques(id) ON DELETE CASCADE, -- If mosque deleted, delete its prayer times
                              UNIQUE (mosque_id, date) -- Only one prayer time entry per mosque per day
);

-- Trigger function to update 'updated_at' timestamp for 'prayer_times' table
CREATE OR REPLACE FUNCTION update_prayer_times_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_prayer_times_updated_at
    BEFORE UPDATE ON prayer_times
    FOR EACH ROW
    EXECUTE FUNCTION update_prayer_times_updated_at();