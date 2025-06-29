import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom'; // Import useParams and Link
import { getMosqueById, getMosqueEvents, getMosqueAnnouncements, getDailyPrayerTimes } from '../services/apiServices.jsx'; // Ensure .js
import AnnouncementFeedItem from '../components/AnnouncementFeedItem.jsx'; // Use existing component

const MosqueDetailPage = () => {
    const { id } = useParams(); // Get mosque ID from URL
    const [mosque, setMosque] = useState(null);
    const [events, setEvents] = useState([]);
    const [announcements, setAnnouncements] = useState([]);
    const [prayerTimes, setPrayerTimes] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState('overview'); // State for active tab

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Fetch Mosque details
                const mosqueData = await getMosqueById(id);
                if (!mosqueData) {
                    setError('Mosque not found.');
                    setLoading(false);
                    return;
                }
                setMosque(mosqueData);

                // Fetch Mosque-specific Events
                const eventsData = await getMosqueEvents(id);
                setEvents(eventsData);

                // Fetch Mosque-specific Announcements
                const announcementsData = await getMosqueAnnouncements(id);
                setAnnouncements(announcementsData);

                // Fetch Daily Prayer Times for Mosque's location (using current date)
                // Note: You might want to store Lat/Long in Mosque entity for more accuracy
                // For now, using hardcoded example coords if not in mosque data
                const currentLat = 40.8528; // Example Lat for Leonia, NJ
                const currentLong = -74.0051; // Example Long for Leonia, NJ
                const today = new Date();
                const formattedDate = `${today.getDate().toString().padStart(2, '0')}-${(today.getMonth() + 1).toString().padStart(2, '0')}-${today.getFullYear()}`;

                const prayerTimesData = await getDailyPrayerTimes(formattedDate, currentLat, currentLong);
                setPrayerTimes(prayerTimesData.data); // AlAdhan API wraps data in a 'data' field

            } catch (err) {
                setError('Failed to load mosque details. Please check if the backend is running and data is present.');
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [id]); // Re-fetch data if ID changes

    if (loading) {
        return <div className="flex-center min-h-screen"><h1 className="header-text">Loading mosque details...</h1></div>;
    }

    if (error) {
        return <div className="flex-center min-h-screen text-red-600"><h1 className="header-text">{error}</h1></div>;
    }

    return (
        <div className="py-8 bg-gray-50 flex-grow">
            <div className="container">
                {/* Mosque Header Section */}
                <div className="card mb-8 p-6 flex flex-col items-center">
                    {mosque.logoUrl && (
                        <img src={mosque.logoUrl} alt={`${mosque.name} Logo`} className="mosque-logo-detail mb-4" />
                    )}
                    <h2 className="header-text text-center mb-2">{mosque.name}</h2>
                    <p className="text-lg text-secondary text-center mb-4">{mosque.address}, {mosque.city}, {mosque.state} {mosque.zipCode}</p>
                    <p className="text-md text-center">{mosque.description}</p>
                    <div className="flex space-x-4 mt-4">
                        <a href={`mailto:${mosque.contactEmail}`} className="button-primary">Contact Email</a>
                        <a href={`tel:${mosque.phoneNumber}`} className="button-outline">Call {mosque.phoneNumber}</a>
                    </div>
                </div>

                {/* Navigation Tabs for Mosque Details */}
                <nav className="mb-8">
                    <ul className="flex border-b border-gray-200">
                        <li className="-mb-px mr-1">
                            <button className={`bg-white inline-block py-2 px-4 font-semibold ${activeTab === 'overview' ? 'border-l border-t border-r rounded-t text-primary' : 'text-secondary hover:text-gray-800'}`}
                                    onClick={() => setActiveTab('overview')}>Overview</button>
                        </li>
                        <li className="-mb-px mr-1">
                            <button className={`bg-white inline-block py-2 px-4 font-semibold ${activeTab === 'events' ? 'border-l border-t border-r rounded-t text-primary' : 'text-secondary hover:text-gray-800'}`}
                                    onClick={() => setActiveTab('events')}>Events</button>
                        </li>
                        <li className="-mb-px mr-1">
                            <button className={`bg-white inline-block py-2 px-4 font-semibold ${activeTab === 'announcements' ? 'border-l border-t border-r rounded-t text-primary' : 'text-secondary hover:text-gray-800'}`}
                                    onClick={() => setActiveTab('announcements')}>Announcements</button>
                        </li>
                        <li className="-mb-px mr-1">
                            <button className={`bg-white inline-block py-2 px-4 font-semibold ${activeTab === 'prayerTimes' ? 'border-l border-t border-r rounded-t text-primary' : 'text-secondary hover:text-gray-800'}`}
                                    onClick={() => setActiveTab('prayerTimes')}>Prayer Times</button>
                        </li>
                        <li className="-mb-px mr-1">
                            <button className={`bg-white inline-block py-2 px-4 font-semibold ${activeTab === 'forms' ? 'border-l border-t border-r rounded-t text-primary' : 'text-secondary hover:text-gray-800'}`}
                                    onClick={() => setActiveTab('forms')}>Forms</button>
                        </li>
                    </ul>
                </nav>

                {/* Tab Content */}
                <div className="bg-white p-6 rounded shadow">
                    {activeTab === 'overview' && (
                        <div>
                            <h3 className="subheader-text mb-4">Mosque Overview</h3>
                            <p className="text-md mb-2"><strong>Address:</strong> {mosque.address}, {mosque.city}, {mosque.state} {mosque.zipCode}</p>
                            <p className="text-md mb-2"><strong>Contact:</strong> {mosque.contactEmail} | {mosque.phoneNumber}</p>
                            <p className="text-md mt-4">{mosque.description}</p>
                        </div>
                    )}

                    {activeTab === 'events' && (
                        <div>
                            <h3 className="subheader-text mb-4">Upcoming Events</h3>
                            {events.length > 0 ? (
                                <div className="grid grid-cols-1 gap-4">
                                    {events.map(event => (
                                        <div key={event.id} className="card p-4">
                                            <h4 className="font-semibold text-lg mb-1">{event.title}</h4>
                                            <p className="text-sm text-secondary mb-2">{new Date(event.eventDate).toLocaleDateString()} at {event.startTime} - {event.location}</p>
                                            <p className="text-sm">{event.description}</p>
                                            {event.registrationRequired && <p className="text-xs text-primary mt-2">Registration Required</p>}
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <p className="text-secondary">No upcoming events found for this mosque.</p>
                            )}
                        </div>
                    )}

                    {activeTab === 'announcements' && (
                        <div>
                            <h3 className="subheader-text mb-4">Mosque Announcements</h3>
                            {announcements.length > 0 ? (
                                <div className="grid grid-cols-1 gap-4">
                                    {announcements.map(announcement => (
                                        <AnnouncementFeedItem key={announcement.id} announcement={announcement} />
                                    ))}
                                </div>
                            ) : (
                                <p className="text-secondary">No announcements found for this mosque.</p>
                            )}
                        </div>
                    )}

                    {activeTab === 'prayerTimes' && (
                        <div>
                            <h3 className="subheader-text mb-4">Daily Prayer Times ({new Date().toLocaleDateString()})</h3>
                            {prayerTimes ? (
                                <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4 text-center">
                                    {Object.entries(prayerTimes.timings).map(([prayerName, time]) => {
                                        // Define the main prayers you want to display prominently
                                        const mainPrayers = ['Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha'];
                                        // Optional prayers like Sunrise, Imsak, Midnight
                                        const otherPrayers = ['Sunrise', 'Imsak', 'Midnight', 'Firstthird', 'Lastthird'];

                                        // Filter out specific prayers that are less commonly displayed as main times
                                        if (!mainPrayers.includes(prayerName) && !otherPrayers.includes(prayerName) && prayerName !== 'Jummah') {
                                            return null; // Skip rendering
                                        }

                                        // Check if it's currently time for this prayer (simple comparison, not perfect for real-time)
                                        const now = new Date();
                                        const [hours, minutes] = time.split(' ')[0].split(':').map(Number);
                                        const prayerTime = new Date(now.getFullYear(), now.getMonth(), now.getDate(), hours, minutes);
                                        const isCurrentPrayer = now > prayerTime && (now - prayerTime) < (30 * 60 * 1000); // Within 30 mins after prayer start

                                        return (
                                            <div key={prayerName} className={`p-4 rounded-lg shadow-sm border ${mainPrayers.includes(prayerName) ? 'bg-primary-light border-primary text-primary-dark' : 'bg-gray-100 border-gray-200 text-secondary'} ${isCurrentPrayer ? 'current-prayer-highlight' : ''}`}>
                                                <p className="font-semibold text-lg mb-1">{prayerName}</p>
                                                <p className="text-2xl font-bold">{time.split(' ')[0]}</p> {/* Time without timezone */}
                                            </div>
                                        );
                                    })}
                                    {/* Add Jummah prayer if available and it's Friday, handle separately as per DDL */}
                                    {new Date().getDay() === 5 && prayerTimes.timings.Jummah && ( // Friday is 5, check if Jummah is in timings
                                        <div className="p-4 rounded-lg shadow-sm border bg-green-100 border-green-500 text-green-800 col-span-full">
                                            <p className="font-semibold text-lg mb-1">Jummah</p>
                                            <p className="text-2xl font-bold">{prayerTimes.timings.Jummah.split(' ')[0]}</p>
                                        </div>
                                    )}
                                </div>
                            ) : (
                                <p className="text-secondary">Failed to load prayer times or no data available for this location today.</p>
                            )}
                            <p className="text-sm text-gray-500 mt-4">Prayer times fetched from AlAdhan API. Calculation Method: {prayerTimes?.meta?.method?.methodName || 'N/A'}</p>
                            <p className="text-sm text-gray-500">Source: {prayerTimes?.meta?.school || 'N/A'}</p> {/* Add school/source info */}
                        </div>
                    )}

                    {activeTab === 'forms' && (
                        <div>
                            <h3 className="subheader-text mb-4">Community Forms & Services</h3>
                            <p className="text-secondary mb-4">
                                Please use the links below to access our external forms for various community services.
                                These forms are managed externally to provide a seamless and specialized experience.
                            </p>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <a href="https://forms.gle/YOUR_MEMBERSHIP_FORM_LINK" target="_blank" rel="noopener noreferrer" className="button-primary text-center">Membership Application</a>
                                <a href="https://forms.gle/YOUR_VOLUNTEER_FORM_LINK" target="_blank" rel="noopener noreferrer" className="button-primary text-center">Volunteer Signup</a>
                                <a href="https://forms.gle/YOUR_FUNERAL_FORM_LINK" target="_blank" rel="noopener noreferrer" className="button-primary text-center">Funeral Service Request</a>
                                <a href="https://forms.gle/YOUR_MARRIAGE_FORM_LINK" target="_blank" rel="noopener noreferrer" className="button-primary text-center">Marriage Service Request</a>
                            </div>
                            <p className="text-xs text-gray-500 mt-4">Note: These links will open external forms.</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default MosqueDetailPage;