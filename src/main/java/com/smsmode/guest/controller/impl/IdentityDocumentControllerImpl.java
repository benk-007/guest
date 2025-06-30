/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.controller.impl;

import com.smsmode.guest.controller.IdentityDocumentController;
import com.smsmode.guest.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.guest.resource.iddocument.IdentityDocumentItemGetResource;
import com.smsmode.guest.resource.iddocument.IdentityDocumentPostResource;
import com.smsmode.guest.service.IdentityDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of IdentificationDocumentController.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@RestController
@RequiredArgsConstructor
public class IdentityDocumentControllerImpl implements IdentityDocumentController {

    private final IdentityDocumentService identityDocumentService;

    @Override
    public ResponseEntity<Page<IdentityDocumentItemGetResource>> getAllIdentityDocumentsByPage(String guestId, String search, Pageable pageable) {
        return identityDocumentService.retrieveAllByGuestId(guestId, search, pageable);
    }

    /**
     * Support multipart pour création avec images
     */
    @Override
    public ResponseEntity<IdentityDocumentItemGetResource> createIdDocument(IdentityDocumentPostResource identityDocumentPostResource, MultipartFile documentImages) {
        return identityDocumentService.create(identityDocumentPostResource, documentImages);
    }


    @Override
    public ResponseEntity<IdentityDocumentItemGetResource> getIdentityDocumentById(String identityDocumentId) {
        return identityDocumentService.retrieveById(identityDocumentId);
    }

    @Override
    public ResponseEntity<Resource> getIdentityDocumentImageById(String identityDocumentId) {
        return identityDocumentService.retrieveImageById(identityDocumentId);
    }

    @Override
    public ResponseEntity<IdentityDocumentItemGetResource> updateIdDocument(String identityDocumentId, IdDocumentPatchResource idDocumentPatchResource, MultipartFile documentImage) {
        return identityDocumentService.updateById(identityDocumentId, idDocumentPatchResource, documentImage);
    }

    /**
     * Suppression avec images
     */
    @Override
    public ResponseEntity<Void> deleteIdDocument(String identityDocumentId) {
        return identityDocumentService.deleteByIdWithImages(identityDocumentId);
    }
}