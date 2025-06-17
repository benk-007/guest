/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.dao.specification;

import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.model.GuestModel_;
import com.smsmode.guest.model.IdentificationDocumentModel;
import com.smsmode.guest.model.IdentificationDocumentModel_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification class for IdentificationDocument entity queries.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
public class IdentificationDocumentSpecification {

    public static Specification<IdentificationDocumentModel> withId(String idDocumentId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(IdentificationDocumentModel_.id), idDocumentId);
    }

    public static Specification<IdentificationDocumentModel> withGuestId(String guestId) {
        return (root, query, criteriaBuilder) -> {
            Join<IdentificationDocumentModel, GuestModel> join = root.join(IdentificationDocumentModel_.guest);
            return criteriaBuilder.equal(join.get(GuestModel_.id), guestId);
        };
    }
}