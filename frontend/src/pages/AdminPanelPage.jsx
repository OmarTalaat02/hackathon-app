// frontend/src/pages/AdminPanelPage.jsx
import React, { useState, useEffect } from 'react';
import { createAnnouncement, getAllMosques, getGlobalAnnouncements, deleteAnnouncement } from '../services/apiServices.jsx'; // Import deleteAnnouncement

const AdminPanelPage = () => {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [mosqueId, setMosqueId] = useState('');
    const [expirationDate, setExpirationDate] = useState('');
    const [isPublished, setIsPublished] = useState(true);
    const [mosques, setMosques] = useState([]);
    const [announcements, setAnnouncements] = useState([]); // New state for existing announcements
    const [message, setMessage] = useState('');
    const [messageType, setMessageType] = useState('');

    // Fetch mosques for the dropdown and existing announcements
    useEffect(() => {
        const fetchInitialData = async () => {
            try {
                const mosquesData = await getAllMosques();
                setMosques(mosquesData);
                if (mosquesData.length > 0) {
                    setMosqueId(mosquesData[0].id);
                }
                const announcementsData = await getGlobalAnnouncements();
                setAnnouncements(announcementsData);
            } catch (error) {
                console.error("Error fetching initial data for admin panel:", error);
                setMessage("Failed to load initial data. Backend might be down.");
                setMessageType('error');
            }
        };
        fetchInitialData();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!title || !content || !mosqueId) {
            setMessage("Title, content, and mosque selection are required.");
            setMessageType('error');
            return;
        }

        try {
            const newAnnouncement = {
                title,
                content,
                mosqueId: Number(mosqueId),
                expirationDate: expirationDate ? `${expirationDate}T23:59:59` : null,
                isPublished: isPublished,
            };

            const savedAnnouncement = await createAnnouncement(newAnnouncement); // Get the saved object
            setMessage('Announcement added successfully!');
            setMessageType('success');
            // Add the newly created announcement to the list without refetching all
            setAnnouncements(prevAnnouncements => [savedAnnouncement, ...prevAnnouncements]);

            setTitle('');
            setContent('');
            setExpirationDate('');
            setIsPublished(true);
            if (mosques.length > 0) {
                setMosqueId(mosques[0].id);
            }

        } catch (error) {
            console.error("Error adding announcement:", error);
            setMessage(`Failed to add announcement: ${error.message || 'Unknown error'}`);
            setMessageType('error');
        }
    };

    const handleDeleteAnnouncement = async (idToDelete) => {
        if (window.confirm("Are you sure you want to delete this announcement?")) {
            try {
                await deleteAnnouncement(idToDelete);
                setMessage('Announcement deleted successfully!');
                setMessageType('success');
                // Filter out the deleted announcement from the state
                setAnnouncements(prevAnnouncements => prevAnnouncements.filter(ann => ann.id !== idToDelete));
            } catch (error) {
                console.error("Error deleting announcement:", error);
                setMessage(`Failed to delete announcement: ${error.message || 'Unknown error'}`);
                setMessageType('error');
            }
        }
    };

    return (
        <div className="py-8 bg-gray-50 flex-grow">
            <div className="container">
                <h2 className="header-text text-center mb-8">Manage Content</h2>

                {/* Announcement Form Section */}
                <div className="card p-6 mb-8">
                    <h3 className="subheader-text mb-4">Add New Announcement</h3>
                    {message && (
                        <div className={`p-3 rounded mb-4 ${messageType === 'success' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                            {message}
                        </div>
                    )}
                    <form onSubmit={handleSubmit} className="grid grid-cols-1 gap-4">
                        {/* Form fields remain the same */}
                        <div>
                            <label htmlFor="announcementTitle" className="block text-sm font-medium text-gray-700">Title</label>
                            <input
                                type="text"
                                id="announcementTitle"
                                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border"
                                value={title}
                                onChange={(e) => setTitle(e.target.value)}
                                required
                            />
                        </div>
                        <div>
                            <label htmlFor="announcementContent" className="block text-sm font-medium text-gray-700">Content</label>
                            <textarea
                                id="announcementContent"
                                rows="4"
                                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border"
                                value={content}
                                onChange={(e) => setContent(e.target.value)}
                                required
                            ></textarea>
                        </div>
                        <div>
                            <label htmlFor="mosqueSelect" className="block text-sm font-medium text-gray-700">Belongs to Mosque</label>
                            <select
                                id="mosqueSelect"
                                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border"
                                value={mosqueId}
                                onChange={(e) => setMosqueId(Number(e.target.value))}
                                required
                            >
                                {mosques.length > 0 ? (
                                    mosques.map(mosque => (
                                        <option key={mosque.id} value={mosque.id}>{mosque.name}</option>
                                    ))
                                ) : (
                                    <option value="">Loading mosques...</option>
                                )}
                            </select>
                        </div>
                        <div>
                            <label htmlFor="expirationDate" className="block text-sm font-medium text-gray-700">Expiration Date (Optional)</label>
                            <input
                                type="date"
                                id="expirationDate"
                                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm p-2 border"
                                value={expirationDate}
                                onChange={(e) => setExpirationDate(e.target.value)}
                            />
                        </div>
                        <div className="flex items-center">
                            <input
                                type="checkbox"
                                id="isPublished"
                                className="h-4 w-4 text-primary rounded border-gray-300"
                                checked={isPublished}
                                onChange={(e) => setIsPublished(e.target.checked)}
                            />
                            <label htmlFor="isPublished" className="ml-2 block text-sm text-gray-900">Publish Immediately</label>
                        </div>
                        <div>
                            <button type="submit" className="button button-primary w-full">Add Announcement</button>
                        </div>
                    </form>
                </div>

                {/* Existing Announcements with Delete Buttons */}
                <div className="card p-6">
                    <h3 className="subheader-text mb-4">Existing Announcements</h3>
                    {announcements.length > 0 ? (
                        <div className="grid grid-cols-1 gap-4">
                            {announcements.map(announcement => (
                                <div key={announcement.id} className="card p-4 flex justify-between items-center">
                                    <div>
                                        <h4 className="font-semibold text-lg">{announcement.title}</h4>
                                        <p className="text-sm text-secondary">From: {announcement.mosqueName}</p>
                                        <p className="text-xs text-gray-500">Published: {new Date(announcement.publishDate).toLocaleDateString()}</p>
                                    </div>
                                    <button
                                        onClick={() => handleDeleteAnnouncement(announcement.id)}
                                        className="button button-outline"
                                        style={{ borderColor: '#dc3545', color: '#dc3545' }} /* Red outline */
                                    >
                                        Delete
                                    </button>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p className="text-secondary">No announcements to manage.</p>
                    )}
                </div>

            </div>
        </div>
    );
};

export default AdminPanelPage;