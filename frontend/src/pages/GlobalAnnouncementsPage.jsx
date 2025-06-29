// frontend/src/pages/GlobalAnnouncementsPage.jsx
import React, { useEffect, useState } from 'react';
import AnnouncementFeedItem from '../components/AnnouncementFeedItem.jsx';
import { getGlobalAnnouncements } from '../services/apiServices.jsx';

const GlobalAnnouncementsPage = () => {
    const [announcements, setAnnouncements] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchAnnouncements = async () => {
            try {
                const data = await getGlobalAnnouncements();
                setAnnouncements(data);
            } catch (err) {
                setError('Failed to load global announcements. Please check if the backend is running.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchAnnouncements();
    }, []);

    if (loading) {
        return (
            <div className="flex-center min-h-screen">
                <h1 className="header-text">Loading Global Announcements...</h1>
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex-center min-h-screen text-red-600">
                <h1 className="header-text">{error}</h1>
            </div>
        );
    }

    return (
        <div className="py-8 bg-gray-50 flex-grow">
            <div className="container">
                <h2 className="header-text text-center mb-8">All Community Announcements</h2>
                {announcements.length > 0 ? (
                    <div className="grid grid-cols-1 gap-4">
                        {announcements.map((announcement) => (
                            <AnnouncementFeedItem key={announcement.id} announcement={announcement} />
                        ))}
                    </div>
                ) : (
                    <p className="text-secondary text-center">No announcements available at this time.</p>
                )}
            </div>
        </div>
    );
};

export default GlobalAnnouncementsPage;