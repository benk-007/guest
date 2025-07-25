/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.media.service;

import com.smsmode.media.model.GuestModel;
import com.smsmode.media.model.IdentityDocumentModel;
import com.smsmode.media.resource.iddocument.IdentityDocumentItemGetResource;
import com.smsmode.media.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.media.resource.iddocument.IdentityDocumentPostResource;
import org.springframework.core.io.Resource;
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
     * Retrieves all identification documents for a guest with pagination and search support
     */
    ResponseEntity<Page<IdentityDocumentItemGetResource>> retrieveAllByGuestId(String guestId, String search, Pageable pageable);


    /**
     * Creates a new identification document for a guest.
     */
    ResponseEntity<IdentityDocumentItemGetResource> create(IdentityDocumentPostResource identityDocumentPostResource, MultipartFile identityDocumentFile);

    IdentityDocumentModel create(GuestModel guestModel, IdentityDocumentModel identityDocumentModel, MultipartFile identityDocumentFile);


    /**
     * Retrieves an identification document by ID.
     */
    ResponseEntity<IdentityDocumentItemGetResource> retrieveById(String identityDocumentId);

    ResponseEntity<Resource> retrieveImageById(String identityDocumentId);

    /**
     * Updates an identification document partially.
     */
    ResponseEntity<IdentityDocumentItemGetResource> updateById(String idDocumentId, IdDocumentPatchResource idDocumentPatchResource, MultipartFile documentImage);

    /**
     * Deletes identity document and associated images.
     */
    ResponseEntity<Void> deleteByIdWithImages(String idDocumentId);

}