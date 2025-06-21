/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.controller.impl;

import com.smsmode.guest.controller.IdentityDocumentController;
import com.smsmode.guest.resource.iddocument.IdDocumentGetResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPostResource;
import com.smsmode.guest.service.IdentityDocumentService;
import lombok.RequiredArgsConstructor;
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

    /**
      Support multipart pour création avec images
     */
    @Override
    public ResponseEntity<IdDocumentGetResource> createIdDocument(String guestId, IdDocumentPostResource idDocumentPostResource, MultipartFile[] documentImages) {
        return identityDocumentService.create(guestId, idDocumentPostResource, documentImages);
    }

    @Override
    public ResponseEntity<Page<IdDocumentGetResource>> getAllIdDocuments(String guestId, String search, Pageable pageable) {
        return identityDocumentService.retrieveAllByGuestId(guestId, search, pageable);
    }

    @Override
    public ResponseEntity<IdDocumentGetResource> getIdDocumentById(String guestId, String idDocumentId) {
        return identityDocumentService.retrieveById(guestId, idDocumentId);
    }

    @Override
    public ResponseEntity<IdDocumentGetResource> updateIdDocument(String guestId, String idDocumentId, IdDocumentPatchResource idDocumentPatchResource) {
        return identityDocumentService.updateById(guestId, idDocumentId, idDocumentPatchResource);
    }
}