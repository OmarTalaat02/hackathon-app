// frontend/src/main.jsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx'; // This imports your main App component
import './index.css'; // Your global styles

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <App /> {/* Renders the App component */}
    </React.StrictMode>,
);