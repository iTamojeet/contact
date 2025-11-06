package com.cms.service.impl;

import com.cms.dto.ContactDTO;
import com.cms.dto.CreateContactRequest;
import com.cms.entity.Contact;
import com.cms.exception.ResourceNotFoundException;
import com.cms.repository.ContactRepository;
import com.cms.service.ContactService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    public Page<ContactDTO> getContactsPagedFiltered(
            int page, int size, String search,
            String sortBy, String sortDir) {

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Contact> contactsPage;

        if (search != null && !search.isBlank()) {
            // naive search by name or email containing search string (adjust as needed)
            contactsPage = contactRepository.findAll(
                    (root, query, cb) -> cb.or(
                            cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%")),
                    pageable);
        } else {
            contactsPage = contactRepository.findAll(pageable);
        }

        return contactsPage.map(this::convertToDto);
    }

    @Override
    public List<ContactDTO> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ContactDTO getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with ID: " + id));
        return convertToDto(contact);
    }

    @Override
    public ContactDTO createContact(CreateContactRequest request) {
        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone()); // No change, but it will now accept a String
        Contact savedContact = contactRepository.save(contact);
        return convertToDto(savedContact);
    }

    @Override
    @Transactional
    public ContactDTO updateContact(Long id, CreateContactRequest request) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with ID " + id));

        // Update fields if present and valid (basic null check or validation can be expanded)
        if(request.getName() != null && !request.getName().isEmpty()) {
            contact.setName(request.getName());
        }
        if(request.getEmail() != null && !request.getEmail().isEmpty()) {
            contact.setEmail(request.getEmail());
        }
        if(request.getPhone() != null && !request.getPhone().isEmpty()) {
            contact.setPhone(request.getPhone());
        }

        Contact updatedContact = contactRepository.save(contact);
        return convertToDto(updatedContact);
    }


    @Override
    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact not found with ID: " + id);
        }
        contactRepository.deleteById(id);
    }

    private ContactDTO convertToDto(Contact contact) {
        ContactDTO dto = new ContactDTO();
        dto.setContactId(contact.getContactId());
        dto.setName(contact.getName());
        dto.setEmail(contact.getEmail());
        dto.setPhone(contact.getPhone());
        return dto;
    }
}