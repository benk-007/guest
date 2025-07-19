/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.media.dao.specification;

import com.smsmode.media.model.DocumentModel;
import com.smsmode.media.model.DocumentModel_;
import com.smsmode.media.model.IdentityDocumentModel;
import com.smsmode.media.model.IdentityDocumentModel_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 20 May 2025</p>
 */
public class DocumentSpecification {
    public static Specification<DocumentModel> withIdentityDocumentId(String identityDocumentId) {
        return (root, query, criteriaBuilder) -> {
            Join<DocumentModel, IdentityDocumentModel> join = root.join(DocumentModel_.identityDocument);
            return criteriaBuilder.equal(join.get(IdentityDocumentModel_.id), identityDocumentId);
        };
    }

    public static Specification<DocumentModel> withId(String documentId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(DocumentModel_.id), documentId);
    }
}
