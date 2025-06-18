/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.model;

import com.smsmode.guest.enumeration.IdentificationDocumentTypeEnum;
import com.smsmode.guest.model.base.AbstractBaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entity representing an identification document for a guest.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
//TODO: change the name of the table to X_ID_DOCUMENT
@Table(name = "X_IDENTIFICATION_DOCUMENT")
public class IdentificationDocumentModel extends AbstractBaseModel {

    //TODO: add new model image like the one we have in unit service and make the image linked to an ID Document,
    //TODO: expose a controller to allow users to upload an ID DOCUMENT with an image

    @Enumerated(EnumType.STRING)
    private IdentificationDocumentTypeEnum type;

    private String documentNumber;

    private LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GUEST_ID")
    private GuestModel guest;
}