package com.smsmode.guest.resource.guest;

import com.smsmode.guest.embeddable.AddressEmbeddable;
import com.smsmode.guest.embeddable.ContactEmbeddable;
import com.smsmode.guest.resource.iddocument.IdDocumentPostResource;
import jakarta.validation.Valid;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GuestPatchResource {
    private String firstName;
    private String lastName;

    @Valid
    private ContactEmbeddable contact;

    @Valid
    private AddressEmbeddable address;

    private LocalDate birthDate;

    @Valid
    private List<IdDocumentPostResource> idDocuments;
}