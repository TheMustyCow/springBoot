package com.example.contactmanager.service;

import com.example.contactmanager.model.Contact;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This is the Service layer. It contains the business logic and manages
 * our in-memory data store (a HashMap).
 *
 * The @Service annotation tells Spring to manage this class as a bean,
 * so it can be injected into the Controller.
 */
@Service
public class ContactService {

    // Our in-memory data store: maps a contact ID to a Contact object
    private final Map<Long, Contact> contacts = new HashMap<>();

    // Generates unique IDs automatically (thread-safe)
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ContactService() {
        // Pre-populate with a few sample contacts so the GET endpoints work immediately
        Contact c1 = new Contact(idGenerator.getAndIncrement(), "Alice", "Smith", "alice@example.com", "555-0101", "123 Maple St");
        Contact c2 = new Contact(idGenerator.getAndIncrement(), "Bob", "Jones", "bob@example.com", "555-0102", "456 Oak Ave");
        Contact c3 = new Contact(idGenerator.getAndIncrement(), "Carol", "White", "carol@example.com", "555-0103", "789 Pine Rd");
        contacts.put(c1.getId(), c1);
        contacts.put(c2.getId(), c2);
        contacts.put(c3.getId(), c3);
    }

    /**
     * Returns all contacts in the system.
     */
    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts.values());
    }

    /**
     * Finds a single contact by its ID.
     * Returns Optional so the caller can decide what to do if not found.
     */
    public Optional<Contact> getContactById(Long id) {
        return Optional.ofNullable(contacts.get(id));
    }

    /**
     * Creates a new contact with a generated ID.
     */
    public Contact createContact(Contact contact) {
        contact.setId(idGenerator.getAndIncrement());
        contacts.put(contact.getId(), contact);
        return contact;
    }

    /**
     * Updates an existing contact.
     */
    public Optional<Contact> updateContact(Long id, Contact contact) {
        if (!contacts.containsKey(id)) {
            return Optional.empty();
        }

        contact.setId(id);
        contacts.put(id, contact);
        return Optional.of(contact);
    }

    /**
     * Deletes a contact by ID.
     */
    public boolean deleteContact(Long id) {
        return contacts.remove(id) != null;
    }

    /**
     * Searches contacts by first name, last name, or email.
     */
    public List<Contact> searchContacts(String query) {
        if (query == null || query.isBlank()) {
            return getAllContacts();
        }

        String normalizedQuery = query.toLowerCase();
        return contacts.values().stream()
                .filter(contact -> containsIgnoreCase(contact.getFirstName(), normalizedQuery)
                        || containsIgnoreCase(contact.getLastName(), normalizedQuery)
                        || containsIgnoreCase(contact.getEmail(), normalizedQuery))
                .toList();
    }

    /**
     * Finds a contact by exact email address.
     */
    public Optional<Contact> getContactByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }

        return contacts.values().stream()
                .filter(contact -> email.equalsIgnoreCase(contact.getEmail()))
                .findFirst();
    }

    private boolean containsIgnoreCase(String value, String normalizedQuery) {
        return value != null && value.toLowerCase().contains(normalizedQuery);
    }
}
