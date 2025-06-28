/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service.impl;

import com.smsmode.guest.dao.service.DocumentDaoService;
import com.smsmode.guest.dao.service.GuestDaoService;
import com.smsmode.guest.dao.service.IdentityDocumentDaoService;
import com.smsmode.guest.dao.specification.GuestSpecification;
import com.smsmode.guest.dao.specification.IdentityDocumentSpecification;
import com.smsmode.guest.exception.InternalServerException;
import com.smsmode.guest.exception.ResourceNotFoundException;
import com.smsmode.guest.exception.enumeration.InternalServerExceptionTitleEnum;
import com.smsmode.guest.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.smsmode.guest.mapper.IdentityDocumentMapper;
import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.model.IdentityDocumentModel;
import com.smsmode.guest.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.guest.resource.iddocument.IdentityDocumentItemGetResource;
import com.smsmode.guest.resource.iddocument.IdentityDocumentPostResource;
import com.smsmode.guest.service.IdentityDocumentService;
import com.smsmode.guest.service.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
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
    private final DocumentDaoService documentDaoService;
    private final StorageService storageService;

    @Override
    public ResponseEntity<Page<IdentityDocumentItemGetResource>> retrieveAllByGuestId(String guestId, String search, Pageable pageable) {
        Specification<IdentityDocumentModel> spec = IdentityDocumentSpecification.withGuestId(guestId).and(IdentityDocumentSpecification.withValueLike(search));
        Page<IdentityDocumentModel> idDocuments = identityDocumentDaoService.findAllBy(spec, pageable);
        return ResponseEntity.ok(idDocuments.map(identityDocumentMapper::modelToItemGetResource));
    }

    /**
     * Création avec support images multipart
     */
    @Override
    @Transactional
    public ResponseEntity<IdentityDocumentItemGetResource> create(IdentityDocumentPostResource idDocumentPostResource, MultipartFile identityDocumentFile) {
        GuestModel guest = guestDaoService.findOneBy(GuestSpecification.withIdEqual(idDocumentPostResource.getGuestId()));
        IdentityDocumentModel identityDocumentModel = this.create(guest, idDocumentPostResource, identityDocumentFile);
        return ResponseEntity.created(URI.create("")).body(identityDocumentMapper.modelToItemGetResource(identityDocumentModel));
    }

    @Override
    @Transactional
    public IdentityDocumentModel create(GuestModel guestModel, IdentityDocumentPostResource idDocumentPostResource, MultipartFile identityDocumentFile) {
        if (!ObjectUtils.isEmpty(idDocumentPostResource)) {
            IdentityDocumentModel identityDocumentModel = identityDocumentMapper.postResourceToModel(idDocumentPostResource);
            identityDocumentModel.setGuest(guestModel);
            identityDocumentModel = identityDocumentDaoService.save(identityDocumentModel);

            if (!ObjectUtils.isEmpty(identityDocumentFile) && !identityDocumentFile.isEmpty()) {
                identityDocumentModel.setFileName(identityDocumentFile.getOriginalFilename());
                identityDocumentModel = identityDocumentDaoService.save(identityDocumentModel);
                String identityDocumentPath = storageService.generateDocumentPath(identityDocumentModel);
                try {
                    String identityDocumentFileName = storageService.storeFile(identityDocumentPath, identityDocumentFile.getInputStream());

                    if (ObjectUtils.isEmpty(identityDocumentFileName)) {
                        throw new InternalServerException(InternalServerExceptionTitleEnum.FILE_UPLOAD, "An unexpected error occurred while saving the identity document file. Please try again later.");
                    }
                } catch (IOException e) {
                    log.warn("An error occurred when storing image file", e);
                    throw new InternalServerException(InternalServerExceptionTitleEnum.FILE_UPLOAD, "An unexpected error occurred while saving the image. Please try again later.");
                }
            }
            return identityDocumentModel;
        } else {
            log.debug("No identity document provided for guest: {}", guestModel.getId());
            return null;
        }

    }


    @Override
    public ResponseEntity<IdentityDocumentItemGetResource> retrieveById(String identityDocumentId) {
        Specification<IdentityDocumentModel> spec = Specification.where(
                IdentityDocumentSpecification.withId(identityDocumentId));
        IdentityDocumentModel identityDocumentModel = identityDocumentDaoService.findOneBy(spec);
        return ResponseEntity.ok(identityDocumentMapper.modelToItemGetResource(identityDocumentModel));
    }

    @Override
    public ResponseEntity<Resource> retrieveImageById(String identityDocumentId) {
        Specification<IdentityDocumentModel> spec = Specification.where(
                IdentityDocumentSpecification.withId(identityDocumentId));
        IdentityDocumentModel identityDocumentModel = identityDocumentDaoService.findOneBy(spec);
        String imagePath = storageService.generateDocumentPath(identityDocumentModel);
        File file = new File(imagePath);
        if (file.exists()) {
            log.debug("file exists");
            Resource resource = null;
            try {
                byte[] bytes = FileUtils.readFileToByteArray(file);
                resource = new InputStreamResource(new ByteArrayInputStream(bytes));
            } catch (IOException e) {
                log.debug("An error has been thrown while to convert {} to byte stream", file);
                throw new InternalServerException(InternalServerExceptionTitleEnum.FILE_UPLOAD, "An error occurred while trying convert file to byte stream");
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + identityDocumentModel.getFileName())
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                    .body(resource);
        } else {
            throw new ResourceNotFoundException(
                    ResourceNotFoundExceptionTitleEnum.IMAGE_NOT_FOUND,
                    "No image found with the specified criteria");
        }
    }

    @Override
    public ResponseEntity<IdentityDocumentItemGetResource> updateById(String identityDocumentId, IdDocumentPatchResource idDocumentPatchResource) {
        log.debug("Updating identity document: {}", identityDocumentId);
        Specification<IdentityDocumentModel> spec = Specification.where(
                IdentityDocumentSpecification.withId(identityDocumentId));
        IdentityDocumentModel existingIdDocument = identityDocumentDaoService.findOneBy(spec);
        IdentityDocumentModel updatedIdDocument = identityDocumentMapper.patchResourceToModel(idDocumentPatchResource, existingIdDocument);
        updatedIdDocument = identityDocumentDaoService.save(updatedIdDocument);
        return ResponseEntity.ok(identityDocumentMapper.modelToItemGetResource(updatedIdDocument));
    }

    /**
     * Suppression avec images associées
     */
    @Override
    @Transactional
    public ResponseEntity<Void> deleteByIdWithImages(String idDocumentId) {

/*        Specification<IdentityDocumentModel> spec = Specification.where(
                        IdentityDocumentSpecification.withId(idDocumentId))
                .and(IdentityDocumentSpecification.withGuestId(guestId));

        if (!identityDocumentDaoService.existsBy(spec)) {
            throw new ResourceNotFoundException(
                    ResourceNotFoundExceptionTitleEnum.ID_DOCUMENT_NOT_FOUND,
                    "Identity document not found with ID: " + idDocumentId);
        }

        // 1. Récupérer l'identity document
        IdentityDocumentModel identityDocument = identityDocumentDaoService.findOneBy(spec);

        // 2. Supprimer toutes les images associées
        Specification<DocumentModel> imageSpec = DocumentSpecification.withIdentityDocumentId(idDocumentId);
        Page<DocumentModel> images = documentDaoService.findAllBy(imageSpec, Pageable.unpaged());

        for (DocumentModel image : images) {
            // Supprimer le fichier physique
            String imagePath = storageService.generateDocumentPath(image);
            storageService.deleteFile(imagePath);

            // Supprimer l'enregistrement
            documentDaoService.deleteBy(DocumentSpecification.withId(image.getId()));
        }

        // 3. Supprimer l'identity document
        identityDocumentDaoService.deleteBy(spec);

        return ResponseEntity.noContent().build();*/
        return null;
    }
}