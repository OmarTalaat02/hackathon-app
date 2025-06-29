import React from 'react';

const AnnouncementFeedItem = ({ announcement }) => {
    if (!announcement) {
        return <div className="card">Loading announcement...</div>;
    }

    const publishDate = new Date(announcement.publishDate).toLocaleDateString('en-US', {
        year: 'numeric', month: 'long', day: 'numeric'
    });

    return (
        <div className="card announcement-feed-item">
            <h4 className="font-semibold text-md mb-1">{announcement.title}</h4>
            {/* Display mosque name directly from the DTO */}
            <p className="text-secondary text-sm mb-2">From: {announcement.mosqueName || 'N/A'}</p>
            <p className="text-sm">{announcement.content.substring(0, 150)}...</p> {/* Truncate content */}
            <p className="text-xs text-gray-500 mt-2">Published: {publishDate}</p>
        </div>
    );
};

export default AnnouncementFeedItem;