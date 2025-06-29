import React, { useEffect, useState } from 'react';
import MosqueCard from '../components/MosqueCard.jsx';
import { getAllMosques } from '../services/apiServices.jsx';

const MosquesPage = () => {
    const [mosques, setMosques] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await getAllMosques();
                setMosques(data);
            } catch (err) {
                setError('Failed to fetch mosques. Please check if the backend is running.');
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
                <h1 className="header-text">Loading Mosques...</h1>
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
                <h2 className="header-text text-center mb-8">Discover All Mosques</h2>
                {loading ? ( // Display loading message while fetching
                    <div className="flex-center"><h1 className="header-text">Loading Mosques...</h1></div>
                ) : error ? ( // Display error message if there's an error
                    <div className="flex-center text-red-600"><h1 className="header-text">{error}</h1></div>
                ) : mosques.length > 0 ? ( // Only render grid if data exists
                    <div className="grid grid-cols-1 gap-6">
                        {mosques.map((mosque) => (
                            <MosqueCard key={mosque.id} mosque={mosque} />
                        ))}
                    </div>
                ) : ( // Display no mosques message if data is empty after loading
                    <p className="text-secondary text-center">No mosques found. Please populate your database.</p>
                )}
            </div>
        </div>
    );
};

export default MosquesPage;