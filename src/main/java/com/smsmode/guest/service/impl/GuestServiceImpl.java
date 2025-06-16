package com.smsmode.guest.service.impl;

import com.smsmode.guest.dao.repository.GuestRepository;
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
 * Implementation of GuestService.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    @Override
    public ResponseEntity<GuestGetResource> create(GuestPostResource guestPostResource) {
        log.debug("Creating new guest: {} {}", guestPostResource.getFirstName(), guestPostResource.getLastName());

        GuestModel guestModel = guestMapper.postResourceToModel(guestPostResource);
        guestModel = guestRepository.save(guestModel);

        return ResponseEntity.created(URI.create("/guests/" + guestModel.getId()))
                .body(guestMapper.modelToGetResource(guestModel));
    }

    @Override
    public ResponseEntity<Page<GuestGetResource>> retrieveAllByPage(String search, Pageable pageable) {
        log.debug("Retrieving guests with search: '{}', page: {}, size: {}",
                search, pageable.getPageNumber(), pageable.getPageSize());

        Specification<GuestModel> spec = createSearchSpecification(search);
        Page<GuestModel> guests = guestRepository.findAll(spec, pageable);

        return ResponseEntity.ok(guests.map(guestMapper::modelToGetResource));
    }

    @Override
    public ResponseEntity<GuestGetResource> retrieveById(String guestId) {
        log.debug("Retrieving guest by ID: {}", guestId);

        return guestRepository.findById(guestId)
                .map(guestMapper::modelToGetResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<GuestGetResource> updateById(String guestId, GuestPatchResource guestPatchResource) {
        log.debug("Updating guest ID: {}", guestId);

        return guestRepository.findById(guestId)
                .map(existingGuest -> {
                    GuestModel updatedGuest = guestMapper.patchResourceToModel(guestPatchResource, existingGuest);
                    updatedGuest = guestRepository.save(updatedGuest);
                    return ResponseEntity.ok(guestMapper.modelToGetResource(updatedGuest));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteById(String guestId) {
        log.debug("Deleting guest ID: {}", guestId);

        if (guestRepository.existsById(guestId)) {
            guestRepository.deleteById(guestId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private Specification<GuestModel> createSearchSpecification(String search) {
        if (search == null || search.trim().isEmpty()) {
            return null;
        }

        String likePattern = "%" + search.toLowerCase() + "%";
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern)
        );
    }
}