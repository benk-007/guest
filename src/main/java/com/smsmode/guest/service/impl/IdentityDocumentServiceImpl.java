/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service.impl;

import com.smsmode.guest.dao.service.DocumentDaoService;
import com.smsmode.guest.dao.service.GuestDaoService;
import com.smsmode.guest.dao.service.IdentityDocumentDaoService;
import com.smsmode.guest.dao.specification.GuestSpecification;
import com.smsmode.guest.dao.specification.DocumentSpecification;
import com.smsmode.guest.dao.specification.IdentityDocumentSpecification;
import com.smsmode.guest.exception.InternalServerException;
import com.smsmode.guest.exception.ResourceNotFoundException;
import com.smsmode.guest.exception.enumeration.InternalServerExceptionTitleEnum;
import com.smsmode.guest.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.smsmode.guest.mapper.IdentityDocumentMapper;
import com.smsmode.guest.model.DocumentModel;
import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.model.IdentityDocumentModel;
import com.smsmode.guest.resource.iddocument.IdDocumentGetResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPostResource;
import com.smsmode.guest.service.IdentityDocumentService;
import com.smsmode.guest.service.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
public class IdentityDocumentServiceImpl implements IdentityDocumentService {

    private final IdentityDocumentDaoService identityDocumentDaoService;
    private final IdentityDocumentMapper identityDocumentMapper;
    private final GuestDaoService guestDaoService;
    private final DocumentDaoService documentDaoService; // ✅ Pour les images
    private final StorageService storageService; // ✅ Pour supprimer les fichiers

    /**
     Création avec support images multipart
     */
    @Override
    @Transactional
    public ResponseEntity<IdDocumentGetResource> create(String guestId, IdDocumentPostResource idDocumentPostResource, MultipartFile[] documentImages) {
        try {
            // 1. Vérifier que le guest existe
            GuestModel guest = guestDaoService.findOneBy(GuestSpecification.withIdEqual(guestId));

            // 2. Créer l'identity document
            IdentityDocumentModel identityDocumentModel = identityDocumentMapper.postResourceToModel(idDocumentPostResource);
            identityDocumentModel.setGuest(guest);
            identityDocumentModel = identityDocumentDaoService.save(identityDocumentModel);

            // 3. Traiter les images si fournies
            if (documentImages != null && documentImages.length > 0) {

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
            }

            return ResponseEntity.created(URI.create("")).body(identityDocumentMapper.modelToGetResource(identityDocumentModel));

        } catch (IOException e) {
            throw new InternalServerException(
                    InternalServerExceptionTitleEnum.FILE_UPLOAD,
                    "Failed to process document images"
            );
        }
    }

    @Override
    public ResponseEntity<Page<IdDocumentGetResource>> retrieveAllByGuestId(String guestId, String search, Pageable pageable) {

        // Vérifier que le guest existe
        if (!guestDaoService.existsById(guestId)) {
            throw new ResourceNotFoundException(
                    ResourceNotFoundExceptionTitleEnum.GUEST_NOT_FOUND,
                    "Guest not found with ID: " + guestId);
        }

        // Construire la spécification avec recherche
        Specification<IdentityDocumentModel> spec = IdentityDocumentSpecification.withGuestId(guestId);

        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and(IdentityDocumentSpecification.withSearch(search));
        }

        Page<IdentityDocumentModel> idDocuments = identityDocumentDaoService.findAllBy(spec, pageable);

        return ResponseEntity.ok(idDocuments.map(identityDocumentMapper::modelToGetResource));
    }

    @Override
    public ResponseEntity<IdDocumentGetResource> retrieveById(String guestId, String idDocumentId) {
        Specification<IdentityDocumentModel> spec = Specification.where(
                        IdentityDocumentSpecification.withId(idDocumentId))
                .and(IdentityDocumentSpecification.withGuestId(guestId));

        IdentityDocumentModel idDocument = identityDocumentDaoService.findOneBy(spec);
        return ResponseEntity.ok(identityDocumentMapper.modelToGetResource(idDocument));
    }

    @Override
    public ResponseEntity<IdDocumentGetResource> updateById(String guestId, String idDocumentId, IdDocumentPatchResource idDocumentPatchResource) {
        log.debug("Updating identity document: {} for guest: {}", idDocumentId, guestId);

        Specification<IdentityDocumentModel> spec = Specification.where(
                        IdentityDocumentSpecification.withId(idDocumentId))
                .and(IdentityDocumentSpecification.withGuestId(guestId));

        IdentityDocumentModel existingIdDocument = identityDocumentDaoService.findOneBy(spec);
        IdentityDocumentModel updatedIdDocument = identityDocumentMapper.patchResourceToModel(idDocumentPatchResource, existingIdDocument);
        updatedIdDocument = identityDocumentDaoService.save(updatedIdDocument);

        return ResponseEntity.ok(identityDocumentMapper.modelToGetResource(updatedIdDocument));
    }
}