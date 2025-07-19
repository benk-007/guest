package com.smsmode.media.resource.guest;

import com.smsmode.media.embeddable.AddressEmbeddable;
import com.smsmode.media.embeddable.ContactEmbeddable;
import jakarta.validation.Valid;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for partial Guest updates (PATCH operations).
 * All fields are optional for partial updates.
 */
@Data
public class GuestPatchResource {
    private String firstName;
    private String lastName;

    @Valid
    private ContactEmbeddable contact;

    @Valid
    private AddressEmbeddable address;

    private LocalDate birthDate;
}