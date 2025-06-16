package com.smsmode.guest.resource.guest;

import com.smsmode.guest.embeddable.AddressEmbeddable;
import com.smsmode.guest.embeddable.ContactEmbeddable;
import com.smsmode.guest.enumeration.IdentificationDocumentTypeEnum;
import com.smsmode.guest.resource.common.AuditGetResource;
import lombok.Data;

@Data
public class GuestGetResource {
    private String id;
    private String firstName;
    private String lastName;
    private ContactEmbeddable contact;
    private AddressEmbeddable address;
    private IdentificationDocumentTypeEnum identificationDocumentType;
    private String identificationNumber;
    private AuditGetResource audit;
}