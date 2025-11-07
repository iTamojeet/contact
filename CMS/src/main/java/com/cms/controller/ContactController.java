package com.cms.controller;

import com.cms.dto.ContactDTO;
import com.cms.dto.CreateContactRequest;
import com.cms.service.ContactService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
@AllArgsConstructor
public class ContactController {

    private static final Logger logger = LogManager.getLogger(ContactController.class);

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<Page<ContactDTO>> getContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        logger.info("Received request to get contacts - page: {}, size: {}, search: {}, sortBy: {}, sortDir: {}",
                page, size, search, sortBy, sortDir);

        Page<ContactDTO> pageResult = contactService.getContactsPagedFiltered(page, size, search, sortBy, sortDir);

        logger.info("Returning {} contacts in response", pageResult.getNumberOfElements());
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        logger.info("Received request to get contact by ID: {}", id);

        ContactDTO contact = contactService.getContactById(id);

        if (contact == null) {
            logger.warn("Contact not found with ID: {}", id);
        } else {
            logger.info("Returning contact: {}", contact);
        }

        return ResponseEntity.ok(contact);
    }

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@Valid @RequestBody CreateContactRequest request) {
        logger.info("Received request to create new contact: {}", request);

        ContactDTO newContact = contactService.createContact(request);

        logger.info("Created new contact with ID: {}", newContact.getContactId());
        return new ResponseEntity<>(newContact, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody CreateContactRequest request) {

        logger.info("Received request to update contact ID: {} with data: {}", id, request);

        ContactDTO updatedContact = contactService.updateContact(id, request);

        if (updatedContact == null) {
            logger.warn("Update failed. Contact not found with ID: {}", id);
        } else {
            logger.info("Successfully updated contact ID: {}", id);
        }

        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        logger.info("Received request to delete contact with ID: {}", id);

        contactService.deleteContact(id);

        logger.info("Deleted contact with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
