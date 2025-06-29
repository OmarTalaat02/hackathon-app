// frontend/src/services/apiService.js

const API_BASE_URL = 'http://localhost:8080/api/v1'; // Your Spring Boot backend URL

// --- Mosque Endpoints ---
export const getAllMosques = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/mosques`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Error fetching mosques:", error);
        throw error;
    }
};

export const getMosqueById = async (id) => {
    try {
        const response = await fetch(`${API_BASE_URL}/mosques/${id}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error(`Error fetching mosque with ID ${id}:`, error);
        throw error;
    }
};

// --- Announcement Endpoints ---
export const getGlobalAnnouncements = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/announcements/feed`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Error fetching global announcements:", error);
        throw error;
    }
};

export const getMosqueAnnouncements = async (mosqueId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/announcements/mosque/${mosqueId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error(`Error fetching announcements for mosque ID ${mosqueId}:`, error);
        throw error;
    }
};

// --- Event Endpoints ---
export const getUpcomingEvents = async () => {
    try {
        const response = await fetch(`${API_BASE_URL}/events/upcoming`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Error fetching upcoming events:", error);
        throw error;
    }
};

export const getMosqueEvents = async (mosqueId) => {
    try {
        const response = await fetch(`${API_BASE_URL}/events/mosque/${mosqueId}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error(`Error fetching events for mosque ID ${mosqueId}:`, error);
        throw error;
    }
};

export const getDailyPrayerTimes = async (date, latitude, longitude, method = 2) => {
    try {
        const response = await fetch(`${API_BASE_URL}/prayer-times/daily?date=${date}&latitude=${latitude}&longitude=${longitude}&method=${method}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Error fetching daily prayer times:", error);
        throw error;
    }
};

export const getMonthlyPrayerTimes = async (year, month, latitude, longitude, method = 2) => {
    try {
        const response = await fetch(`${API_BASE_URL}/prayer-times/monthly?year=${year}&month=${month}&latitude=${latitude}&longitude=${longitude}&method=${method}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Error fetching monthly prayer times:", error);
        throw error;
    }
};

export const createAnnouncement = async (announcementData) => {
    try {
        const response = await fetch(`${API_BASE_URL}/announcements`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(announcementData),
        });
        if (!response.ok) {
            const errorText = await response.text(); // Get more detailed error message from backend
            throw new Error(`HTTP error! status: ${response.status} - ${errorText}`);
        }
        return await response.json();
    } catch (error) {
        console.error("Error creating announcement:", error);
        throw error;
    }
};

// --- Delete Announcement Endpoint ---
export const deleteAnnouncement = async (id) => {
    try {
        const response = await fetch(`${API_BASE_URL}/announcements/${id}`, {
            method: 'DELETE',
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        // No content returned for 204 No Content
    } catch (error) {
        console.error(`Error deleting announcement with ID ${id}:`, error);
        throw error;
    }
};