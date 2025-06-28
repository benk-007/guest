package com.smsmode.guest.resource.iddocument;

import com.smsmode.guest.enumeration.IdentityDocumentTypeEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IdDocumentPatchResource {
    private IdentityDocumentTypeEnum type;
    private String value;
    private LocalDate expirationDate;
}