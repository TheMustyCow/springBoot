package com.example.contactmanager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.contactmanager.model.Contact;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This is the Service layer. It contains the business logic and manages
 * our JSON-backed contact store.
 *
 * The @Service annotation tells Spring to manage this class as a bean,
 * so it can be injected into the Controller.
 */
@Service
public class ContactService {

    private static final Path CONTACTS_FILE = Path.of("contacts.json");

    // Maps a contact ID to a Contact object.
    private final Map<Long, Contact> contacts = new LinkedHashMap<>();

    // Generates unique IDs automatically (thread-safe)
    private final AtomicLong idGenerator = new AtomicLong(1);

    private final ObjectMapper objectMapper;

    public ContactService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        loadContacts();
    }

    /**
     * Returns all contacts in the system.
     */
    public synchronized List<Contact> getAllContacts() {
        return new ArrayList<>(contacts.values());
    }

    /**
     * Finds a single contact by its ID.
     * Returns Optional so the caller can decide what to do if not found.
     */
    public synchronized Optional<Contact> getContactById(Long id) {
        return Optional.ofNullable(contacts.get(id));
    }

    /**
     * Creates a new contact with a generated ID.
     */
    public synchronized Contact createContact(Contact contact) {
        contact.setId(idGenerator.getAndIncrement());
        contacts.put(contact.getId(), contact);
        saveContacts();
        return contact;
    }

    /**
     * Updates an existing contact.
     */
    public synchronized Optional<Contact> updateContact(Long id, Contact contact) {
        if (!contacts.containsKey(id)) {
            return Optional.empty();
        }

        contact.setId(id);
        contacts.put(id, contact);
        saveContacts();
        return Optional.of(contact);
    }

    /**
     * Deletes a contact by ID.
     */
    public synchronized boolean deleteContact(Long id) {
        boolean removed = contacts.remove(id) != null;
        if (removed) {
            saveContacts();
        }
        return removed;
    }

    /**
     * Searches contacts by first name, last name, or email.
     */
    public synchronized List<Contact> searchContacts(String query) {
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
    public synchronized Optional<Contact> getContactByEmail(String email) {
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

    private void loadContacts() {
        try {
            List<Contact> loadedContacts = objectMapper.readValue(
                    CONTACTS_FILE.toFile(),
                    new TypeReference<List<Contact>>() {
                    }
            );

            long nextId = 1;
            for (Contact contact : loadedContacts) {
                contacts.put(contact.getId(), contact);
                nextId = Math.max(nextId, contact.getId() + 1);
            }
            idGenerator.set(nextId);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + CONTACTS_FILE.toAbsolutePath(), e);
        }
    }

    private void saveContacts() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(CONTACTS_FILE.toFile(), contacts.values());
        } catch (IOException e) {
            throw new IllegalStateException("Could not write " + CONTACTS_FILE.toAbsolutePath(), e);
        }
    }
}
