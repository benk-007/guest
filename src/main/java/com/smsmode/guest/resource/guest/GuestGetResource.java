package com.smsmode.guest.resource.guest;

import com.smsmode.guest.embeddable.AddressEmbeddable;
import com.smsmode.guest.embeddable.ContactEmbeddable;
import com.smsmode.guest.resource.common.AuditGetResource;
import com.smsmode.guest.resource.iddocument.IdDocumentGetResource;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GuestGetResource {
    private String id;
    private String firstName;
    private String lastName;
    private ContactEmbeddable contact;
    private AddressEmbeddable address;
    private LocalDate birthDate;
    private List<IdDocumentGetResource> idDocuments;
    private AuditGetResource audit;
}