package com.smsmode.guest.resource.iddocument;

import com.smsmode.guest.enumeration.IdentificationDocumentTypeEnum;
import com.smsmode.guest.resource.common.AuditGetResource;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IdDocumentGetResource {
    private String id;
    private IdentificationDocumentTypeEnum type;
    private String documentNumber;
    private LocalDate expirationDate;
    private AuditGetResource audit;
}