/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.controller;

import com.smsmode.guest.resource.iddocument.IdDocumentGetResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPostResource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for IdentificationDocument operations.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@RequestMapping("/id-documents")
public interface IdentificationDocumentController {

    /**
     * Creates a new identification document for a guest.
     */
    @PostMapping
    ResponseEntity<IdDocumentGetResource> createIdDocument(
            @PathVariable("guestId") String guestId,
            @RequestBody @Valid IdDocumentPostResource idDocumentPostResource);

    /**
     * Retrieves all identification documents for a guest with pagination.
     */
    @GetMapping
    ResponseEntity<Page<IdDocumentGetResource>> getAllIdDocuments(
            @PathVariable("guestId") String guestId,
            Pageable pageable);

    /**
     * Retrieves an identification document by ID.
     */
    @GetMapping("/{idDocumentId}")
    ResponseEntity<IdDocumentGetResource> getIdDocumentById(
            @PathVariable("guestId") String guestId,
            @PathVariable("idDocumentId") String idDocumentId);

    /**
     * Updates an identification document partially.
     */
    @PatchMapping("/{idDocumentId}")
    ResponseEntity<IdDocumentGetResource> updateIdDocument(
            @PathVariable("guestId") String guestId,
            @PathVariable("idDocumentId") String idDocumentId,
            @RequestBody @Valid IdDocumentPatchResource idDocumentPatchResource);

    /**
     * Deletes an identification document.
     */
    @DeleteMapping("/{idDocumentId}")
    ResponseEntity<Void> deleteIdDocument(
            @PathVariable("guestId") String guestId,
            @PathVariable("idDocumentId") String idDocumentId);
}