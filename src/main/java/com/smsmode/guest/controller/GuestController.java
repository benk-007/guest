package com.smsmode.guest.controller;

import com.smsmode.guest.resource.guest.GuestGetResource;
import com.smsmode.guest.resource.guest.GuestPatchResource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller for Guest operations.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@RequestMapping("/guests")
public interface GuestController {

    /**
     * Creates a new guest with optional documents and images.
     * Handles all cases: guest alone, guest+idDocument, guest+idDocument+images
     */
    @PostMapping(consumes = "multipart/form-data")
    ResponseEntity<GuestGetResource> createGuest(
            @RequestParam("guestJson") String guestJson,
            @RequestParam(value = "documentImage", required = false) MultipartFile[] documentImages);


    /**
     * Retrieves all guests with optional search and pagination.
     */
    @GetMapping
    ResponseEntity<Page<GuestGetResource>> getAllGuests(
            @RequestParam(value = "search", required = false) String search,
            Pageable pageable);

    /**
     * Retrieves a guest by ID.
     */
    @GetMapping("/{guestId}")
    ResponseEntity<GuestGetResource> getGuestById(@PathVariable("guestId") String guestId);

    /**
     * Updates a guest partially.
     */
    @PatchMapping("/{guestId}")
    ResponseEntity<GuestGetResource> updateGuest(
            @PathVariable("guestId") String guestId,
            @RequestBody @Valid GuestPatchResource guestPatchResource);

    /**
     * Deletes a guest.
     */
    @DeleteMapping("/{guestId}")
    ResponseEntity<Void> deleteGuest(@PathVariable("guestId") String guestId);
}