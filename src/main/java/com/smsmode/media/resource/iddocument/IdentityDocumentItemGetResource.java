package com.smsmode.media.resource.iddocument;

import com.smsmode.media.enumeration.IdentityDocumentTypeEnum;
import com.smsmode.media.resource.common.AuditGetResource;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IdentityDocumentItemGetResource {
    private String id;
    private IdentityDocumentTypeEnum type;
    private String value;
    private LocalDate expirationDate;
    private boolean fileProvided;
    private AuditGetResource audit;
}