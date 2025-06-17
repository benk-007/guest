package com.smsmode.guest.dao.service;

import com.smsmode.guest.model.IdentificationDocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * DAO service interface for IdentificationDocument operations.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
public interface IdentificationDocumentDaoService {

    IdentificationDocumentModel save(IdentificationDocumentModel idDocument);

    IdentificationDocumentModel findOneBy(Specification<IdentificationDocumentModel> specification);

    Page<IdentificationDocumentModel> findAllBy(Specification<IdentificationDocumentModel> specification, Pageable pageable);

    boolean existsBy(Specification<IdentificationDocumentModel> specification);

    void deleteBy(Specification<IdentificationDocumentModel> specification);
}