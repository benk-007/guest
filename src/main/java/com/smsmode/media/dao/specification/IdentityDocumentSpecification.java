/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.media.dao.specification;

import com.smsmode.media.model.GuestModel;
import com.smsmode.media.model.GuestModel_;
import com.smsmode.media.model.IdentityDocumentModel;
import com.smsmode.media.model.IdentityDocumentModel_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

/**
 * Specification class for IdentificationDocument entity queries.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
public class IdentityDocumentSpecification {

    public static Specification<IdentityDocumentModel> withId(String idDocumentId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(IdentityDocumentModel_.id), idDocumentId);
    }

    public static Specification<IdentityDocumentModel> withGuestId(String guestId) {
        return (root, query, criteriaBuilder) -> {
            Join<IdentityDocumentModel, GuestModel> join = root.join(IdentityDocumentModel_.guest);
            return criteriaBuilder.equal(join.get(GuestModel_.id), guestId);
        };
    }

    public static Specification<IdentityDocumentModel> withValueLike(String value) {
        return (root, query, criteriaBuilder) ->
                ObjectUtils.isEmpty(value) ? criteriaBuilder.conjunction() :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(IdentityDocumentModel_.value)),
                                "%".concat(value.toLowerCase()).concat("%"));

    }

    public static Specification<IdentityDocumentModel> withFile() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get(IdentityDocumentModel_.fileName));
    }
}