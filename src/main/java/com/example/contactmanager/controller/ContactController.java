package com.example.contactmanager.controller;

import com.example.contactmanager.model.Contact;
import com.example.contactmanager.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is the REST Controller. It handles incoming HTTP requests and maps
 * them to our service methods.
 *
 * The @RestController annotation tells Spring this class handles web requests.
 * The @RequestMapping("/api/contacts") means every route in this class starts with /api/contacts.
 *
 * The ContactService is injected automatically by Spring through the constructor
 * (this is called "constructor injection").
 */
@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // ========================================================================
    // FULLY IMPLEMENTED ENDPOINTS
    // ========================================================================

    /**
     * GET /api/contacts
     * Returns a list of all contacts.
     */
    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    /**
     * GET /api/contacts/{id}
     * Returns a single contact by ID, or 404 if not found.
     *
     * The {id} in the path is captured by @PathVariable and passed as a Long.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        return contactService.getContactById(id)
                .map(ResponseEntity::ok)                       // if found, wrap in 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // if not found, return 404
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        Contact created = contactService.createContact(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact) {
        return contactService.updateContact(id, contact)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        if (contactService.deleteContact(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Contact>> searchContacts(@RequestParam("q") String query) {
        return ResponseEntity.ok(contactService.searchContacts(query));
    }

    @GetMapping("/email")
    public ResponseEntity<Contact> getContactByEmail(@RequestParam("email") String email) {
        return contactService.getContactByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
