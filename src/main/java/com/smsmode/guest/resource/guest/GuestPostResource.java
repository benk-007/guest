package com.smsmode.guest.resource.guest;

import com.smsmode.guest.embeddable.AddressEmbeddable;
import com.smsmode.guest.embeddable.ContactEmbeddable;
import com.smsmode.guest.enumeration.IdentificationDocumentTypeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GuestPostResource {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Contact information is required")
    @Valid
    private ContactEmbeddable contact;

    @Valid
    private AddressEmbeddable address;

    private IdentificationDocumentTypeEnum identificationDocumentType;
    private String identificationNumber;
}