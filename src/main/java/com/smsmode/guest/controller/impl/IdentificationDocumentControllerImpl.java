/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.controller.impl;

import com.smsmode.guest.controller.IdentificationDocumentController;
import com.smsmode.guest.resource.iddocument.IdDocumentGetResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPostResource;
import com.smsmode.guest.service.IdentificationDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of IdentificationDocumentController.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@RestController
@RequiredArgsConstructor
public class IdentificationDocumentControllerImpl implements IdentificationDocumentController {

    private final IdentificationDocumentService identificationDocumentService;

    @Override
    public ResponseEntity<IdDocumentGetResource> createIdDocument(String guestId, IdDocumentPostResource idDocumentPostResource) {
        return identificationDocumentService.create(guestId, idDocumentPostResource);
    }

    @Override
    public ResponseEntity<Page<IdDocumentGetResource>> getAllIdDocuments(String guestId, Pageable pageable) {
        return identificationDocumentService.retrieveAllByGuestId(guestId, pageable);
    }

    @Override
    public ResponseEntity<IdDocumentGetResource> getIdDocumentById(String guestId, String idDocumentId) {
        return identificationDocumentService.retrieveById(guestId, idDocumentId);
    }

    @Override
    public ResponseEntity<IdDocumentGetResource> updateIdDocument(String guestId, String idDocumentId, IdDocumentPatchResource idDocumentPatchResource) {
        return identificationDocumentService.updateById(guestId, idDocumentId, idDocumentPatchResource);
    }

    @Override
    public ResponseEntity<Void> deleteIdDocument(String guestId, String idDocumentId) {
        return identificationDocumentService.deleteById(guestId, idDocumentId);
    }
}