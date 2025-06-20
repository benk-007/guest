/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.dao.service;

import com.smsmode.guest.model.DocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 19 May 2025</p>
 */
public interface DocumentDaoService {

    boolean existsBy(Specification<DocumentModel> specification);

    DocumentModel save(DocumentModel image);

    void deleteBy(Specification<DocumentModel> specification);

    Page<DocumentModel> findAllBy(Specification<DocumentModel> specification, Pageable pageable);

    DocumentModel findOneBy(Specification<DocumentModel> imageModelSpecification);

}
