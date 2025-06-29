import './index.css'; // Make sure this imports your main CSS file

function App() {
    return (
        <div className="app-container"> {/* Using a simple class now */}
            <h1 style={{ color: 'navy', fontSize: '3rem', fontWeight: 'bold' }}> {/* Inline style for quick test */}
                Hello MosqueLink Frontend!
            </h1>
        </div>
    );
}

export default App;