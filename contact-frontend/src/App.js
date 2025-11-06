import React, { useState } from 'react';
import ContactList from './components/ContactList';
import ContactForm from './components/ContactForm';
import './App.css';

function App() {
    const [refreshKey, setRefreshKey] = useState(0);
    const [currentContact, setCurrentContact] = useState(null);

    const handleContactAddedOrUpdated = () => {
        setRefreshKey(prevKey => prevKey + 1);
        setCurrentContact(null); // Clear the form after an operation
    };

    const handleContactSelected = (contact) => {
        setCurrentContact(contact);
    };

    return (
        <div className="App">
            <header className="App-header">
                <h1>Contact Management System</h1>
            </header>
            <main>
                <div className="content-section">
                    <ContactForm 
                        currentContact={currentContact} 
                        onContactAddedOrUpdated={handleContactAddedOrUpdated} 
                    />
                </div>
                <div className="content-section">
                    <ContactList 
                        onContactSelected={handleContactSelected} 
                        refreshKey={refreshKey} 
                    />
                </div>
            </main>
        </div>
    );
}

export default App;