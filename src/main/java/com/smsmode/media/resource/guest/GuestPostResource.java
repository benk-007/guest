package com.smsmode.media.resource.guest;

import com.smsmode.media.embeddable.AddressEmbeddable;
import com.smsmode.media.embeddable.ContactEmbeddable;
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

    private LocalDate birthDate;

    @NotNull
    @Valid
    private ContactEmbeddable contact;

    @Valid
    private AddressEmbeddable address;


    @Valid
    private GuestIdDocumentPostResource identityDocument;
}