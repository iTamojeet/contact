import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './Contact.css';

const ContactForm = ({ currentContact, onContactAddedOrUpdated }) => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        phone: ''
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    // This useEffect hook populates the form when a contact is selected for editing
    useEffect(() => {
        if (currentContact) {
            setFormData({
                name: currentContact.name,
                email: currentContact.email,
                phone: currentContact.phone,
            });
        } else {
            // Reset form if no contact is selected (e.g., after an update)
            setFormData({ name: '', email: '', phone: '' });
        }
    }, [currentContact]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setSuccess(null);

        try {
            if (currentContact) {
                // Update existing contact
                await axios.put(`/api/contacts/${currentContact.contactId}`, formData);
                setSuccess('Contact updated successfully!');
            } else {
                // Create a new contact
                await axios.post('/api/contacts', formData);
                setSuccess('Contact added successfully!');
                setFormData({ name: '', email: '', phone: '' });
            }
            onContactAddedOrUpdated();
        } catch (err) {
            console.error('API Error:', err.response || err);
            const backendErrorMessage = err.response?.data?.errors?.phone || err.response?.data?.message || 'An unknown error occurred.';
            setError(backendErrorMessage);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="contact-form-container">
            <h2>{currentContact ? 'Edit Contact' : 'Add New Contact'}</h2>
            <form onSubmit={handleSubmit}>
                {success && <div className="alert-success">{success}</div>}
                {error && <div className="alert-error">{error}</div>}
                
                <div className="form-group">
                    <label htmlFor="name">Name:</label>
                    <input 
                        type="text" 
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required 
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input 
                        type="email" 
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required 
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="phone">Phone:</label>
                    <input 
                        type="tel"
                        id="phone"
                        name="phone"
                        value={formData.phone}
                        onChange={handleChange}
                        required 
                    />
                </div>
                <button type="submit" disabled={loading}>
                    {loading ? (currentContact ? 'Updating...' : 'Adding...') : (currentContact ? 'Update Contact' : 'Add Contact')}
                </button>
            </form>
        </div>
    );
};

export default ContactForm;