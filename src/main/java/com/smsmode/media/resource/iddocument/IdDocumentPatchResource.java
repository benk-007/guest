package com.smsmode.media.resource.iddocument;

import com.smsmode.media.enumeration.IdentityDocumentTypeEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IdDocumentPatchResource {
    private IdentityDocumentTypeEnum type;
    private String value;
    private LocalDate expirationDate;
}