import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Contact.css";

const ContactList = ({ onContactSelected, refreshKey }) => {
  const [contacts, setContacts] = useState([]);
  const [error, setError] = useState(null);

  // Pagination and sorting states
  const [page, setPage] = useState(0);
  const [size] = useState(10); // page size fixed or make adjustable
  const [totalPages, setTotalPages] = useState(0);

  const [searchQuery, setSearchQuery] = useState("");
  const [sortBy, setSortBy] = useState("name");
  const [sortDir, setSortDir] = useState("asc");

  const fetchContacts = async () => {
    try {
      const response = await axios.get("/api/contacts", {
        params: {
          page,
          size,
          search: searchQuery,
          sortBy,
          sortDir,
        },
      });
      setContacts(response.data.content);
      setTotalPages(response.data.totalPages);
      setError(null);
    } catch (err) {
      console.error("There was an error fetching the contacts!", err);
      setError(
        "Failed to load contacts. Please ensure the backend is running."
      );
    }
  };

  useEffect(() => {
    fetchContacts();
  }, [page, refreshKey, searchQuery, sortBy, sortDir]);

  const handleDelete = async (contactId) => {
    try {
      await axios.delete(`/api/contacts/${contactId}`);
      fetchContacts();
    } catch (err) {
      console.error("Failed to delete contact:", err);
      setError("Failed to delete contact.");
    }
  };

  return (
    <div>
      {/* Search input */}
      <input
        type="text"
        placeholder="Search by name or email"
        value={searchQuery}
        onChange={(e) => {
          setPage(0); // reset page
          setSearchQuery(e.target.value);
        }}
      />

      {/* Sort selectors */}
      <select value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
        <option value="name">Sort by Name</option>
        <option value="email">Sort by Email</option>
      </select>

      <select value={sortDir} onChange={(e) => setSortDir(e.target.value)}>
        <option value="asc">Ascending</option>
        <option value="desc">Descending</option>
      </select>

      {/* Contacts table */}
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {contacts.map((contact) => (
            <tr key={contact.id}>
              <td>{contact.name}</td>
              <td>{contact.email}</td>
              <td>{contact.phone}</td>
              <td>
                <button onClick={() => onContactSelected(contact)}>Edit</button>
                <button onClick={() => handleDelete(contact.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Pagination controls */}
      <div>
        <button disabled={page <= 0} onClick={() => setPage(page - 1)}>
          Prev
        </button>
        {[...Array(totalPages).keys()].map((p) => (
          <button
            key={p}
            style={{ fontWeight: p === page ? "bold" : "normal" }}
            onClick={() => setPage(p)}
          >
            {p + 1}
          </button>
        ))}
        <button
          disabled={page >= totalPages - 1}
          onClick={() => setPage(page + 1)}
        >
          Next
        </button>
      </div>

      {error && <div style={{ color: "red" }}>{error}</div>}
    </div>
  );
};

export default ContactList;
