/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service.impl;

import com.smsmode.guest.dao.service.GuestDaoService;
import com.smsmode.guest.dao.specification.GuestSpecification;
import com.smsmode.guest.mapper.GuestMapper;
import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.resource.guest.GuestItemGetResource;
import com.smsmode.guest.resource.guest.GuestPatchResource;
import com.smsmode.guest.resource.guest.GuestPostResource;
import com.smsmode.guest.service.GuestService;
import com.smsmode.guest.service.IdentityDocumentService;
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

    @Override
    @Transactional
    public ResponseEntity<GuestItemGetResource> create(GuestPostResource guestPostResource, MultipartFile identityDocumentFile) {
        GuestModel guestModel = guestMapper.postResourceToModel(guestPostResource);
        guestModel = guestDaoService.save(guestModel);
        identityDocumentService.create(guestModel, guestPostResource.getIdentityDocument(), identityDocumentFile);
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

/*        if (!guestDaoService.existsById(guestId)) {
            return ResponseEntity.notFound().build();
        }

        // 1. Supprimer tous les identity documents avec leurs images
//        Specification<IdentityDocumentModel> idDocSpec = IdentityDocumentSpecification.withGuestId(guestId);
        Specification<IdentityDocumentModel> idDocSpec = null;
        Page<IdentityDocumentModel> identityDocuments = identityDocumentDaoService.findAllBy(idDocSpec, Pageable.unpaged());

        for (IdentityDocumentModel idDoc : identityDocuments) {
            // Supprimer les images de chaque identity document
            Specification<DocumentModel> imageSpec = DocumentSpecification.withIdentityDocumentId(idDoc.getId());
            Page<DocumentModel> images = documentDaoService.findAllBy(imageSpec, Pageable.unpaged());

            for (DocumentModel image : images) {
                // Supprimer le fichier physique
                String imagePath = storageService.generateDocumentPath(image);
                storageService.deleteFile(imagePath);

                // Supprimer l'enregistrement
                documentDaoService.deleteBy(DocumentSpecification.withId(image.getId()));
            }

            // Supprimer l'identity document
            identityDocumentDaoService.deleteBy(IdentityDocumentSpecification.withId(idDoc.getId()));
        }

        // 2. Supprimer le guest
        guestDaoService.deleteById(guestId);

        return ResponseEntity.noContent().build();*/
        return null;
    }
}