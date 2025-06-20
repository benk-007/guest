/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smsmode.guest.dao.service.GuestDaoService;
import com.smsmode.guest.dao.service.IdentityDocumentDaoService;
import com.smsmode.guest.dao.service.DocumentDaoService;
import com.smsmode.guest.dao.specification.GuestSpecification;
import com.smsmode.guest.exception.InternalServerException;
import com.smsmode.guest.exception.enumeration.InternalServerExceptionTitleEnum;
import com.smsmode.guest.mapper.GuestMapper;
import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.model.IdentityDocumentModel;
import com.smsmode.guest.model.DocumentModel;
import com.smsmode.guest.resource.guest.GuestGetResource;
import com.smsmode.guest.resource.guest.GuestPatchResource;
import com.smsmode.guest.resource.guest.GuestPostResource;
import com.smsmode.guest.service.GuestService;
import com.smsmode.guest.service.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final IdentityDocumentDaoService identityDocumentDaoService;

    private final DocumentDaoService documentDaoService;
    private final StorageService storageService;

    @Override
    @Transactional
    public ResponseEntity<GuestGetResource> create(GuestPostResource guestPostResource, MultipartFile[] documentImages) {
        try {
            // 1. Créer le guest
            GuestModel guestModel = guestMapper.postResourceToModel(guestPostResource);
            guestModel = guestDaoService.save(guestModel);

            // 2. Vérifier avec ObjectUtils.isEmpty
            IdentityDocumentModel identityDocumentModel = null;
            if (!ObjectUtils.isEmpty(guestPostResource.getIdentityDocument())) {

                identityDocumentModel = guestMapper.identityDocumentPostToModel(guestPostResource.getIdentityDocument());
                identityDocumentModel.setGuest(guestModel);
                identityDocumentModel = identityDocumentDaoService.save(identityDocumentModel);

            } else {
                log.debug("No identity document provided for guest: {}", guestModel.getId());
            }

            // 3. Traiter les images si fournies ET si identityDocument existe
            if (documentImages != null && documentImages.length > 0 && identityDocumentModel != null) {

                for (MultipartFile file : documentImages) {
                    if (!file.isEmpty()) {

                        // Créer le DocumentModel
                        DocumentModel documentModel = new DocumentModel();
                        documentModel.setFileName(file.getOriginalFilename());
                        documentModel.setIdentityDocument(identityDocumentModel);
                        documentModel = documentDaoService.save(documentModel);

                        // Sauvegarder le fichier
                        String imagePath = storageService.generateDocumentPath(documentModel);
                        String savedFileName = storageService.storeFile(imagePath, file.getInputStream());

                        if (savedFileName == null) {
                            throw new InternalServerException(
                                    InternalServerExceptionTitleEnum.FILE_UPLOAD,
                                    "Failed to save document image: " + file.getOriginalFilename()
                            );
                        }
                    }
                }
            } else if (documentImages != null && documentImages.length > 0 && identityDocumentModel == null) {
                log.warn("Document images provided but no identity document created. Images will be ignored.");
            }

            return ResponseEntity.created(URI.create("")).body(guestMapper.modelToGetResource(guestModel));

        } catch (IOException e) {
            throw new InternalServerException(
                    InternalServerExceptionTitleEnum.FILE_UPLOAD,
                    "Failed to process document images"
            );
        }
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