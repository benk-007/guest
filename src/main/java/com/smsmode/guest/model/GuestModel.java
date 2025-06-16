/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.model;

import com.smsmode.guest.embeddable.AddressEmbeddable;
import com.smsmode.guest.embeddable.ContactEmbeddable;
import com.smsmode.guest.enumeration.IdentificationDocumentTypeEnum;
import com.smsmode.guest.model.base.AbstractBaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a Guest in the PMS system.
 * A guest is a person who makes a reservation at the hotel.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "X_GUEST")
public class GuestModel extends AbstractBaseModel {

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "CONTACT_EMAIL", nullable = false, unique = true)),
            @AttributeOverride(name = "mobile", column = @Column(name = "CONTACT_MOBILE", nullable = false))
    })
    private ContactEmbeddable contact;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street1", column = @Column(name = "ADDRESS_STREET1")),
            @AttributeOverride(name = "street2", column = @Column(name = "ADDRESS_STREET2")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "ADDRESS_POSTAL_CODE")),
            @AttributeOverride(name = "city", column = @Column(name = "ADDRESS_CITY")),
            @AttributeOverride(name = "country", column = @Column(name = "ADDRESS_COUNTRY"))
    })
    private AddressEmbeddable address;

    @Enumerated(EnumType.STRING)
    @Column(name = "IDENTIFICATION_DOCUMENT_TYPE")
    private IdentificationDocumentTypeEnum identificationDocumentType;

    @Column(name = "IDENTIFICATION_NUMBER")
    private String identificationNumber;
}
