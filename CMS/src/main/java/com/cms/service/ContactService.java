
package com.cms.service;

import com.cms.dto.ContactDTO;
import com.cms.dto.CreateContactRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactService {
    Page<ContactDTO> getContactsPagedFiltered(
            int page, int size, String search, String sortBy, String sortDir);
    List<ContactDTO> getAllContacts();
    ContactDTO getContactById(Long id);
    ContactDTO createContact(CreateContactRequest request);
    ContactDTO updateContact(Long id, CreateContactRequest request);
    void deleteContact(Long id);
}