/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service.impl;

import com.smsmode.guest.dao.service.GuestDaoService;
import com.smsmode.guest.dao.specification.GuestSpecification;
import com.smsmode.guest.mapper.GuestMapper;
import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.resource.guest.GuestGetResource;
import com.smsmode.guest.resource.guest.GuestPatchResource;
import com.smsmode.guest.resource.guest.GuestPostResource;
import com.smsmode.guest.service.GuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestDaoService guestDaoService;
    private final GuestMapper guestMapper;

    @Override
    public ResponseEntity<GuestGetResource> create(GuestPostResource guestPostResource) {

        GuestModel guestModel = guestMapper.postResourceToModel(guestPostResource);

        guestModel = guestDaoService.save(guestModel);

        return ResponseEntity.created(URI.create("")).body(guestMapper.modelToGetResource(guestModel));
    }

    @Override
    public ResponseEntity<Page<GuestGetResource>> retrieveAllByPage(String search, Pageable pageable) {

        Specification<GuestModel> spec = GuestSpecification.withSearch(search);
        Page<GuestModel> guests = guestDaoService.findAllBy(spec, pageable);

        return ResponseEntity.ok(guests.map(guestMapper::modelToGetResource));
    }

    @Override
    public ResponseEntity<GuestGetResource> retrieveById(String guestId) {
        GuestModel guest = guestDaoService.findOneBy(GuestSpecification.withIdEqual(guestId));
        return ResponseEntity.ok(guestMapper.modelToGetResource(guest));
    }

    @Override
    public ResponseEntity<GuestGetResource> updateById(String guestId, GuestPatchResource guestPatchResource) {

        GuestModel existingGuest = guestDaoService.findOneBy(GuestSpecification.withIdEqual(guestId));
        GuestModel updatedGuest = guestMapper.patchResourceToModel(guestPatchResource, existingGuest);
        updatedGuest = guestDaoService.save(updatedGuest);

        return ResponseEntity.ok(guestMapper.modelToGetResource(updatedGuest));
    }

    @Override
    public ResponseEntity<Void> deleteById(String guestId) {

        if (guestDaoService.existsById(guestId)) {
            guestDaoService.deleteById(guestId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}