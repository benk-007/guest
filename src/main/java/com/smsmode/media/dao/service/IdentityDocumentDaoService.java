package com.smsmode.media.dao.service;

import com.smsmode.media.model.IdentityDocumentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * DAO service interface for IdentificationDocument operations.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
public interface IdentityDocumentDaoService {

    IdentityDocumentModel save(IdentityDocumentModel idDocument);

    IdentityDocumentModel findOneBy(Specification<IdentityDocumentModel> specification);

    Page<IdentityDocumentModel> findAllBy(Specification<IdentityDocumentModel> specification, Pageable pageable);

    boolean existsBy(Specification<IdentityDocumentModel> specification);

    void deleteBy(Specification<IdentityDocumentModel> specification);

    void delete(IdentityDocumentModel idDocument);
}