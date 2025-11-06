package com.cms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "CONTACT")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_seq_generator")
    @SequenceGenerator(name = "contact_seq_generator", sequenceName = "CONTACT_SEQ", allocationSize = 1)
    private Long contactId;
    private String name;
    private String email;
    private String phone;
}

