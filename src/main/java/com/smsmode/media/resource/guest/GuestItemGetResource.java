package com.smsmode.media.resource.guest;

import com.smsmode.media.embeddable.AddressEmbeddable;
import com.smsmode.media.embeddable.ContactEmbeddable;
import com.smsmode.media.resource.common.AuditGetResource;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuestItemGetResource {
    private String id;
    private String firstName;
    private String lastName;
    private ContactEmbeddable contact;
    private AddressEmbeddable address;
    private LocalDate birthDate;
    private boolean withIdentityDocument;
    private AuditGetResource audit;
}