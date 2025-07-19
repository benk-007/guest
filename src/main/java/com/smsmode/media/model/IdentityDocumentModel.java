/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.media.model;

import com.smsmode.media.enumeration.IdentityDocumentTypeEnum;
import com.smsmode.media.model.base.AbstractBaseModel;
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
@Table(name = "X_IDENTITY_DOCUMENT")
public class IdentityDocumentModel extends AbstractBaseModel {
    @Enumerated(EnumType.STRING)
    private IdentityDocumentTypeEnum type;

    private String value;

    private LocalDate expirationDate;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private GuestModel guest;
}