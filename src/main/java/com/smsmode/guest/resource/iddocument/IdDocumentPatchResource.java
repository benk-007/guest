package com.smsmode.guest.resource.iddocument;

import com.smsmode.guest.enumeration.IdentificationDocumentTypeEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IdDocumentPatchResource {
    private IdentificationDocumentTypeEnum type;
    private String documentNumber;
    private LocalDate expirationDate;
}