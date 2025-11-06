
package com.cms.controller;

import com.cms.dto.ContactDTO;
import com.cms.dto.CreateContactRequest;
import com.cms.service.ContactService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<Page<ContactDTO>> getContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<ContactDTO> pageResult = contactService.getContactsPagedFiltered(page, size, search, sortBy, sortDir);
        return ResponseEntity.ok(pageResult);
    }

//    @GetMapping
//    public ResponseEntity<List<ContactDTO>> getAllContacts() {
//        List<ContactDTO> contacts = contactService.getAllContacts();
//        return ResponseEntity.ok(contacts);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        ContactDTO contact = contactService.getContactById(id);
        return ResponseEntity.ok(contact);
    }

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@Valid @RequestBody CreateContactRequest request) {
        ContactDTO newContact = contactService.createContact(request);
        return new ResponseEntity<>(newContact, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody CreateContactRequest request) {
        ContactDTO updatedContact = contactService.updateContact(id, request);
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}