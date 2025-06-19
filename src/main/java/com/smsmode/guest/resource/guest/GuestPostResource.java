package com.smsmode.guest.resource.guest;

import com.smsmode.guest.embeddable.AddressEmbeddable;
import com.smsmode.guest.embeddable.ContactEmbeddable;
import com.smsmode.guest.resource.iddocument.IdDocumentPostResource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GuestPostResource {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Valid
    private ContactEmbeddable contact;

    @Valid
    private AddressEmbeddable address;

    private LocalDate birthDate;

    @Valid
    private IdDocumentPostResource idDocument;
}