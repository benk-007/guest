/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.dao.specification;

import com.smsmode.guest.model.ImageModel;
import com.smsmode.guest.model.ImageModel_;
import com.smsmode.guest.model.IdentificationDocumentModel;
import com.smsmode.guest.model.IdentificationDocumentModel_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 20 May 2025</p>
 */
public class ImageSpecification {
    public static Specification<ImageModel> withIdDocumentId(String idDocumentId) {
        return (root, query, criteriaBuilder) -> {
            Join<ImageModel, IdentificationDocumentModel> join = root.join(ImageModel_.idDocument);
            return criteriaBuilder.equal(join.get(IdentificationDocumentModel_.id), idDocumentId);
        };
    }

    public static Specification<ImageModel> withId(String imageId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(ImageModel_.id), imageId);
    }

    public static Specification<ImageModel> withCover(boolean cover) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(ImageModel_.cover), cover);
    }
}
