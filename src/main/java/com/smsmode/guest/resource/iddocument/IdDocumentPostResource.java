package com.smsmode.guest.resource.iddocument;

import com.smsmode.guest.enumeration.IdentificationDocumentTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IdDocumentPostResource {
    @NotNull
    private IdentificationDocumentTypeEnum type;

    private String documentNumber;
    private LocalDate expirationDate;
}
