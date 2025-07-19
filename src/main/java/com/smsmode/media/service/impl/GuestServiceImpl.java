/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.media.service.impl;

import com.smsmode.media.dao.service.GuestDaoService;
import com.smsmode.media.dao.service.IdentityDocumentDaoService;
import com.smsmode.media.dao.specification.GuestSpecification;
import com.smsmode.media.dao.specification.IdentityDocumentSpecification;
import com.smsmode.media.mapper.GuestMapper;
import com.smsmode.media.mapper.IdentityDocumentMapper;
import com.smsmode.media.model.GuestModel;
import com.smsmode.media.model.IdentityDocumentModel;
import com.smsmode.media.resource.guest.GuestItemGetResource;
import com.smsmode.media.resource.guest.GuestPatchResource;
import com.smsmode.media.resource.guest.GuestPostResource;
import com.smsmode.media.service.GuestService;
import com.smsmode.media.service.IdentityDocumentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private final GuestMapper guestMapper;
    private final GuestDaoService guestDaoService;
    private final IdentityDocumentService identityDocumentService;
    private final IdentityDocumentDaoService identityDocumentDaoService;
    private final IdentityDocumentMapper identityDocumentMapper;

    @Override
    @Transactional
    public ResponseEntity<GuestItemGetResource> create(GuestPostResource guestPostResource, MultipartFile identityDocumentFile) {
        GuestModel guestModel = guestMapper.postResourceToModel(guestPostResource);
        guestModel = guestDaoService.save(guestModel);
        IdentityDocumentModel identityDocumentModel = identityDocumentMapper.postResourceToModel(guestPostResource.getIdentityDocument());
        identityDocumentService.create(guestModel, identityDocumentModel, identityDocumentFile);
        return ResponseEntity.created(URI.create("")).body(guestMapper.modelToGetResource(guestModel));
    }

    @Override
    public ResponseEntity<Page<GuestItemGetResource>> retrieveAllByPage(String search, Pageable pageable) {
        Specification<GuestModel> specification = GuestSpecification.withFirstNameLike(search)
                .or(GuestSpecification.withLastNameLike(search))
                .or(GuestSpecification.withEmailLike(search));
        Page<GuestModel> guests = guestDaoService.findAllBy(specification, pageable);
        return ResponseEntity.ok(guests.map(guestMapper::modelToGetResource));
    }

    @Override
    public ResponseEntity<GuestItemGetResource> retrieveById(String guestId) {
        GuestModel guest = guestDaoService.findOneBy(GuestSpecification.withIdEqual(guestId));
        return ResponseEntity.ok(guestMapper.modelToGetResource(guest));
    }

    @Override
    public ResponseEntity<GuestItemGetResource> updateById(String guestId, GuestPatchResource guestPatchResource) {

        // 1. Récupérer le guest existant
        GuestModel existingGuest = guestDaoService.findOneBy(GuestSpecification.withIdEqual(guestId));

        // 2. Appliquer les modifications partielles
        GuestModel updatedGuest = guestMapper.patchResourceToModel(guestPatchResource, existingGuest);

        // 3. Sauvegarder les modifications
        updatedGuest = guestDaoService.save(updatedGuest);

        return ResponseEntity.ok(guestMapper.modelToGetResource(updatedGuest));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteById(String guestId) {
        GuestModel guest = guestDaoService.findOneBy(GuestSpecification.withIdEqual(guestId));
        Page<IdentityDocumentModel> identityDocumentModels = identityDocumentDaoService.findAllBy(IdentityDocumentSpecification.withGuestId(guestId), Pageable.unpaged());
        for (IdentityDocumentModel identityDocumentModel : identityDocumentModels.getContent()) {
            identityDocumentService.deleteByIdWithImages(identityDocumentModel.getId());
        }
        guestDaoService.delete(guest);
        return ResponseEntity.ok().build();
    }
}