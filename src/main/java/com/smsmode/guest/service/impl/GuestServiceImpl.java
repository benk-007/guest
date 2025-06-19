/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsmode.guest.dao.service.GuestDaoService;
import com.smsmode.guest.dao.service.IdentificationDocumentDaoService;
import com.smsmode.guest.dao.service.ImageDaoService;
import com.smsmode.guest.exception.InternalServerException;
import com.smsmode.guest.exception.enumeration.InternalServerExceptionTitleEnum;
import com.smsmode.guest.model.IdentificationDocumentModel;
import com.smsmode.guest.dao.specification.GuestSpecification;
import com.smsmode.guest.mapper.GuestMapper;
import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.model.ImageModel;
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
    private final IdentificationDocumentDaoService identificationDocumentDaoService;

    private final ImageDaoService imageDaoService;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ResponseEntity<GuestGetResource> create(String guestJson, MultipartFile[] documentImages) {
        try {
            GuestPostResource guestPostResource = objectMapper.readValue(guestJson, GuestPostResource.class);

            GuestModel guestModel = guestMapper.postResourceToModel(guestPostResource);

            guestModel = guestDaoService.save(guestModel);

            IdentificationDocumentModel idDocumentModel = null;
            if (guestPostResource.getIdDocument() != null) {

                idDocumentModel = guestMapper.idDocumentPostToModel(guestPostResource.getIdDocument());
                idDocumentModel.setGuest(guestModel);
                idDocumentModel = identificationDocumentDaoService.save(idDocumentModel);

            }

            if (documentImages != null && documentImages.length > 0 && idDocumentModel != null) {
                for (int i = 0; i < documentImages.length; i++) {
                    MultipartFile file = documentImages[i];
                    if (!file.isEmpty()) {

                        ImageModel imageModel = new ImageModel();
                        imageModel.setFileName(file.getOriginalFilename());
                        imageModel.setIdDocument(idDocumentModel);
                        imageModel.setCover(i == 0); // Premier image = cover
                        imageModel = imageDaoService.save(imageModel);

                        String imagePath = storageService.generateIdDocumentImagePath(imageModel);
                        String savedFileName = storageService.storeFile(imagePath, file.getInputStream());

                        if (savedFileName == null) {
                            throw new InternalServerException(
                                    InternalServerExceptionTitleEnum.FILE_UPLOAD,
                                    "Failed to save document image: " + file.getOriginalFilename()
                            );
                        }
                    }
                }
            } else if (documentImages != null && documentImages.length > 0 && idDocumentModel == null) {
                log.warn("Document images provided but no idDocument created. Images will be ignored.");
            }

            return ResponseEntity.created(URI.create("")).body(guestMapper.modelToGetResource(guestModel));

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid guest data format", e);
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