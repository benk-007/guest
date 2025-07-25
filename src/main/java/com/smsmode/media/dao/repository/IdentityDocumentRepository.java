package com.smsmode.media.dao.repository;

import com.smsmode.media.model.IdentityDocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for IdentificationDocument entity operations.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@Repository
public interface IdentityDocumentRepository extends JpaRepository<IdentityDocumentModel, String>, JpaSpecificationExecutor<IdentityDocumentModel> {
}