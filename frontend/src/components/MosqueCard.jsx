// frontend/src/components/MosqueCard.jsx
import React from 'react';
import { Link } from 'react-router-dom';

const MosqueCard = ({ mosque }) => {
    if (!mosque) {
        return <div className="card">Loading mosque...</div>;
    }

    return (
        <div className="card mosque-card">
            <Link to={`/mosques/${mosque.id}`}>
                {mosque.logoUrl && (
                    <img src={mosque.logoUrl} alt={`${mosque.name} Logo`} className="mosque-logo" />
                )}
                <h3 className="subheader-text text-center mt-2">{mosque.name}</h3>
                <p className="text-secondary text-center text-sm">{mosque.city}, {mosque.state}</p>
                <p className="text-sm mt-2">{mosque.description.substring(0, 100)}...</p> {/* Truncate description */}
                <button className="button-primary w-full mt-4">View Details</button>
            </Link>
        </div>
    );
};

export default MosqueCard;