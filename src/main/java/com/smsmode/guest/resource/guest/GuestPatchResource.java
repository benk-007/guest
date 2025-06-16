package com.smsmode.guest.resource.guest;

import com.smsmode.guest.embeddable.AddressEmbeddable;
import com.smsmode.guest.embeddable.ContactEmbeddable;
import com.smsmode.guest.enumeration.IdentificationDocumentTypeEnum;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class GuestPatchResource {
    private String firstName;
    private String lastName;

    @Valid
    private ContactEmbeddable contact;

    @Valid
    private AddressEmbeddable address;

    private IdentificationDocumentTypeEnum identificationDocumentType;
    private String identificationNumber;
}