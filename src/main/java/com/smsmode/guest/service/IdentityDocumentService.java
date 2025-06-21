/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service;

import com.smsmode.guest.resource.iddocument.IdDocumentGetResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPostResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for IdentificationDocument business operations.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
public interface IdentityDocumentService {

    /**
     * Creates a new identification document for a guest.
     */
    ResponseEntity<IdDocumentGetResource> create(String guestId, IdDocumentPostResource idDocumentPostResource, MultipartFile[] documentImages);

    /**
     * Retrieves all identification documents for a guest with pagination and search support
     */
    ResponseEntity<Page<IdDocumentGetResource>> retrieveAllByGuestId(String guestId, String search, Pageable pageable);

    /**
     * Retrieves an identification document by ID.
     */
    ResponseEntity<IdDocumentGetResource> retrieveById(String guestId, String idDocumentId);

    /**
     * Updates an identification document partially.
     */
    ResponseEntity<IdDocumentGetResource> updateById(String guestId, String idDocumentId, IdDocumentPatchResource idDocumentPatchResource);

    /**
    Deletes identity document and associated images.
     */
    ResponseEntity<Void> deleteByIdWithImages(String guestId, String idDocumentId);
}