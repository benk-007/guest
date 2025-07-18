/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.dao.specification;

import com.smsmode.guest.model.SegmentModel;
import com.smsmode.guest.model.SegmentModel_;
import org.springframework.data.jpa.domain.Specification;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 18 Jul 2025</p>
 */
public class SegmentSpecification {

    public static Specification<SegmentModel> withIdEqual(String segmentId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(SegmentModel_.id), segmentId);
    }

    public static Specification<SegmentModel> withParent(Boolean withParent) {
        return (root, query, criteriaBuilder) ->
                withParent == null ? criteriaBuilder.conjunction() :
                        ((withParent.booleanValue()) ? criteriaBuilder.isNotNull(root.get(SegmentModel_.parent)) :
                                criteriaBuilder.isNull(root.get(SegmentModel_.parent)));
    }
}
