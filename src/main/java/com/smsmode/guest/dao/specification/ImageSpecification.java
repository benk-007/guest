/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.dao.specification;

import com.smsmode.guest.model.DocumentModel;
import com.smsmode.guest.model.DocumentModel_;
import com.smsmode.guest.model.IdentityDocumentModel;
import com.smsmode.guest.model.IdentityDocumentModel_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 20 May 2025</p>
 */
public class ImageSpecification {
    public static Specification<DocumentModel> withIdDocumentId(String idDocumentId) {
        return (root, query, criteriaBuilder) -> {
            Join<DocumentModel, IdentityDocumentModel> join = root.join(DocumentModel_.identityDocument);
            return criteriaBuilder.equal(join.get(IdentityDocumentModel_.id), idDocumentId);
        };
    }

    public static Specification<DocumentModel> withId(String imageId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(DocumentModel_.id), imageId);
    }
}
