/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.model;

import com.smsmode.guest.embeddable.AddressEmbeddable;
import com.smsmode.guest.embeddable.ContactEmbeddable;
import com.smsmode.guest.model.base.AbstractBaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private String firstName;

    private String lastName;

    @Embedded
    private ContactEmbeddable contact;

    @Embedded
    private AddressEmbeddable address;

    private LocalDate birthDate;

    //TODO: remove this relationShip make it unidirectional. The guest model doesn't have to know the id_documents keep the guest relation in the document model
    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<IdentificationDocumentModel> idDocuments = new ArrayList<>();

    //TODO: remove these two below helper methods we don't need them since we will not use the bidirectional relationship anymore
    // Helper methods pour gérer la relation bidirectionnelle
    public void addIdDocument(IdentificationDocumentModel idDocument) {
        idDocuments.add(idDocument);
        idDocument.setGuest(this);
    }

    public void removeIdDocument(IdentificationDocumentModel idDocument) {
        idDocuments.remove(idDocument);
        idDocument.setGuest(null);
    }
}