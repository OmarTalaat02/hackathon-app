// frontend/src/pages/HomePage.jsx
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom'; // Keep Link if needed for internal links on homepage itself
import MosqueCard from "../components/MosqueCard.jsx";
import AnnouncementFeedItem from "../components/AnnouncementFeedItem.jsx";
import { getAllMosques, getGlobalAnnouncements } from "../services/apiServices.jsx";
// Removed "../index.css" import here, as main.jsx/App.jsx imports it globally.

const HomePage = () => {
    const [mosques, setMosques] = useState([]);
    const [announcements, setAnnouncements] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const mosquesData = await getAllMosques();
                setMosques(mosquesData);

                const announcementsData = await getGlobalAnnouncements();
                setAnnouncements(announcementsData);
            } catch (err) {
                setError('Failed to fetch data. Please check if the backend is running and accessible.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) {
        return (
            <div className="flex-center min-h-screen">
                <h1 className="header-text">Loading...</h1>
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
        // HomePage now directly provides its content to the <main> tag in App.jsx.
        // It uses flex-grow to ensure it takes up available vertical space
        // and bg-gray-50 for its background, as per your global styling goals.
        <div className="py-8 bg-gray-50 flex-grow">
            <div className="container">
                <h2 className="header-text text-center mb-8">Connect with Your Local Mosques</h2>

                {/* Stacked Layout for Mosques */}
                <div className="grid grid-cols-1 gap-6 mb-8">
                    {mosques.length > 0 ? (
                        mosques.map((mosque) => (
                            <MosqueCard key={mosque.id} mosque={mosque} />
                        ))
                    ) : (
                        <div className="text-center text-secondary">No mosques found. Please populate your database.</div>
                    )}
                </div>

                {/* Stacked Global Announcements Feed */}
                <div className="card h-full flex flex-col">
                    <h3 className="subheader-text mb-4 border-b pb-2">Global Announcements</h3>
                    <div
                        className="overflow-y-auto flex-grow announcement-scroll-feed-full"> {/* Changed class name to ensure full scroll */}
                        {announcements.length > 0 ? (
                            announcements.map((announcement) => (
                                <AnnouncementFeedItem key={announcement.id} announcement={announcement}/>
                            ))
                        ) : (
                            <p className="text-secondary text-sm">No new announcements from any mosque.</p>
                        )}
                    </div>
                </div>

            </div>
        </div>
    );
};

export default HomePage;