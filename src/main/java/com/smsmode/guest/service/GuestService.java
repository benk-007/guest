package com.smsmode.guest.service;

import com.smsmode.guest.resource.guest.GuestGetResource;
import com.smsmode.guest.resource.guest.GuestPatchResource;
import com.smsmode.guest.resource.guest.GuestPostResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


/**
 * Service interface for Guest business operations.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
public interface GuestService {

    /**
     * Creates a new guest.
     */
    ResponseEntity<GuestGetResource> create(GuestPostResource guestPostResource, MultipartFile documentImages);
    /**
     * Retrieves all guests with pagination and optional search.
     */
    ResponseEntity<Page<GuestGetResource>> retrieveAllByPage(String search, Pageable pageable);

    /**
     * Retrieves a guest by ID.
     */
    ResponseEntity<GuestGetResource> retrieveById(String guestId);

    /**
     * Updates a guest partially.
     */
    ResponseEntity<GuestGetResource> updateById(String guestId, GuestPatchResource guestPatchResource);

    /**
     * Deletes a guest by ID.
     */
    ResponseEntity<Void> deleteById(String guestId);

}