package com.smsmode.guest.controller.impl;

import com.smsmode.guest.controller.GuestController;
import com.smsmode.guest.resource.guest.GuestGetResource;
import com.smsmode.guest.resource.guest.GuestPatchResource;
import com.smsmode.guest.resource.guest.GuestPostResource;
import com.smsmode.guest.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of GuestController.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@RestController
@RequiredArgsConstructor
public class GuestControllerImpl implements GuestController {

    private final GuestService guestService;

    @Override
    public ResponseEntity<GuestGetResource> createGuest(GuestPostResource guestPostResource, MultipartFile[] documentImages) {
        return guestService.create(guestPostResource, documentImages);
    }

    @Override
    public ResponseEntity<Page<GuestGetResource>> getAllGuests(String search, Pageable pageable) {
        return guestService.retrieveAllByPage(search, pageable);
    }

    @Override
    public ResponseEntity<GuestGetResource> getGuestById(String guestId) {
        return guestService.retrieveById(guestId);
    }

    @Override
    public ResponseEntity<GuestGetResource> updateGuest(String guestId, GuestPatchResource guestPatchResource) {
        return guestService.updateById(guestId, guestPatchResource);
    }

    @Override
    public ResponseEntity<Void> deleteGuest(String guestId) {
        return guestService.deleteById(guestId);
    }
}