// frontend/src/App.jsx (relevant section in header and routes)
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link, NavLink } from 'react-router-dom'; // Import NavLink
import './index.css';
import HomePage from './pages/HomePage.jsx';
import MosqueDetailPage from './pages/MosqueDetailPage.jsx';
import GlobalAnnouncementsPage from './pages/GlobalAnnouncementsPage.jsx';
import MosquesPage from './pages/MosquesPage.jsx';
import AdminPanelPage from './pages/AdminPanelPage.jsx'; // Import AdminPanelPage

function App() {
    return (
        <Router>
            <header className="w-full py-4 bg-white shadow-sm">
                <div className="container flex justify-between items-center">
                    <h1 className="text-2xl font-bold text-primary">MosqueLink</h1>
                    <nav>
                        <ul className="flex space-x-4">
                            <li><NavLink to="/" className={({ isActive }) => isActive ? "button button-primary active-nav-link" : "button button-primary"}>Home</NavLink></li>
                            <li><NavLink to="/mosques" className={({ isActive }) => isActive ? "button button-primary active-nav-link" : "button button-primary"}>All Mosques</NavLink></li>
                            <li><NavLink to="/announcements" className={({ isActive }) => isActive ? "button button-primary active-nav-link" : "button button-primary"}>Global Feed</NavLink></li>
                            <li><NavLink to="/admin" className={({ isActive }) => isActive ? "button button-primary active-nav-link" : "button button-primary"}>Admin</NavLink></li> {/* New Admin link */}
                        </ul>
                    </nav>
                </div>
            </header>

            <main className="flex-grow">
                <Routes>
                    <Route path="/" element={<HomePage />} />
                    <Route path="/mosques" element={<MosquesPage />} />
                    <Route path="/mosques/:id" element={<MosqueDetailPage />} />
                    <Route path="/announcements" element={<GlobalAnnouncementsPage />} />
                    <Route path="/admin" element={<AdminPanelPage />} /> {/* New Admin route */}
                    <Route path="*" element={<div className="app-container flex-center min-h-screen"><h1 className="header-text">404 - Not Found</h1></div>} />
                </Routes>
            </main>

            <footer className="w-full py-4 bg-white shadow-sm mt-auto">
                <div className="container text-center text-secondary text-sm">
                    &copy; {new Date().getFullYear()} MosqueLink. All rights reserved.
                </div>
            </footer>
        </Router>
    );
}

export default App;