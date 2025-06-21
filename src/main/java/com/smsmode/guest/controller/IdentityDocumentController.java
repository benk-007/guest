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
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller for IdentificationDocument operations.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@RequestMapping("/id-documents")
public interface IdentityDocumentController {

    /**
     * Creates a new identity document with optional images (multipart support).
     */
    @PostMapping(consumes = "multipart/form-data")
    ResponseEntity<IdDocumentGetResource> createIdDocument(
            @RequestParam("guestId") String guestId,
            @RequestPart("payload") @Valid IdDocumentPostResource idDocumentPostResource,
            @RequestPart(value = "file", required = false) MultipartFile[] documentImages);

    /**
     Retrieves all identity documents for a guest with pagination and search.
     */
    @GetMapping
    ResponseEntity<Page<IdDocumentGetResource>> getAllIdDocuments(
            @RequestParam("guestId") String guestId,
            @RequestParam(value = "search", required = false) String search,
            Pageable pageable);


    /**
     * Retrieves an identification document by ID.
     */
    @GetMapping("/{idDocumentId}")
    ResponseEntity<IdDocumentGetResource> getIdDocumentById(
            @RequestParam("guestId") String guestId,
            @PathVariable("idDocumentId") String idDocumentId);

    /**
     * Updates an identity document partially (without images).
     */
    @PatchMapping("/{idDocumentId}")
    ResponseEntity<IdDocumentGetResource> updateIdDocument(
            @RequestParam("guestId") String guestId,
            @PathVariable("idDocumentId") String idDocumentId,
            @RequestBody @Valid IdDocumentPatchResource idDocumentPatchResource);

    /**
     Deletes an identity document and its associated images.
     */
    @DeleteMapping("/{idDocumentId}")
    ResponseEntity<Void> deleteIdDocument(
            @RequestParam("guestId") String guestId,
            @PathVariable("idDocumentId") String idDocumentId);
}
