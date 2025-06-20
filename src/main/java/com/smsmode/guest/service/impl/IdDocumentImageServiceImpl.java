/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service.impl;

import com.smsmode.guest.dao.service.DocumentDaoService;
import com.smsmode.guest.dao.service.IdentityDocumentDaoService;
import com.smsmode.guest.dao.specification.ImageSpecification;
import com.smsmode.guest.dao.specification.IdentificationDocumentSpecification;
import com.smsmode.guest.exception.InternalServerException;
import com.smsmode.guest.exception.ResourceNotFoundException;
import com.smsmode.guest.exception.enumeration.InternalServerExceptionTitleEnum;
import com.smsmode.guest.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.smsmode.guest.mapper.ImageMapper;
import com.smsmode.guest.model.IdentityDocumentModel;
import com.smsmode.guest.model.DocumentModel;
import com.smsmode.guest.resource.image.ImageGetResource;
import com.smsmode.guest.resource.image.ImagePatchResource;
import com.smsmode.guest.service.IdDocumentImageService;
import com.smsmode.guest.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
 * <p>Created 19 May 2025</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IdDocumentImageServiceImpl implements IdDocumentImageService {

    private final IdentityDocumentDaoService identificationDocumentDaoService;
    private final DocumentDaoService documentDaoService;
    private final StorageService storageService;
    private final ImageMapper imageMapper;

    @Override
    public ResponseEntity<Page<ImageGetResource>> retrieveImages(String idDocumentId, Pageable pageable) {
        Page<DocumentModel> imageModels = documentDaoService.findAllBy(ImageSpecification.withIdDocumentId(idDocumentId), pageable);
        return ResponseEntity.ok(imageModels.map(imageMapper::modelToImageGetResource));
    }

    @Override
    public ResponseEntity<Resource> retrieveImage(String imageId) {

        DocumentModel image = documentDaoService.findOneBy(ImageSpecification.withId(imageId));

        String imagePath = storageService.generateDocumentPath(image);
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
                    .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + image.getFileName())
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                    .body(resource);
        } else {
            throw new ResourceNotFoundException(
                    ResourceNotFoundExceptionTitleEnum.IMAGE_NOT_FOUND,
                    "No image found with the specified criteria");
        }
    }

    @Override
    public ResponseEntity<ImageGetResource> createImage(String idDocumentId, MultipartFile file) {

        IdentityDocumentModel idDocument = identificationDocumentDaoService.findOneBy(IdentificationDocumentSpecification.withId(idDocumentId));

        DocumentModel image = new DocumentModel();
        image.setFileName(file.getOriginalFilename());
        image.setIdentityDocument(idDocument);

        image = documentDaoService.save(image);

        String imagePath = storageService.generateDocumentPath(image);

        try {
            String imageFileName = storageService.storeFile(imagePath, file.getInputStream());
            if (ObjectUtils.isEmpty(imageFileName)) {
                documentDaoService.deleteBy(ImageSpecification.withId(image.getId()));
                throw new InternalServerException(InternalServerExceptionTitleEnum.FILE_UPLOAD, "An unexpected error occurred while saving the image. Please try again later.");
            }
        } catch (IOException e) {
            log.warn("An error occurred when storing image file", e);
            documentDaoService.deleteBy(ImageSpecification.withId(image.getId()));
            throw new InternalServerException(InternalServerExceptionTitleEnum.FILE_UPLOAD, "An unexpected error occurred while saving the image. Please try again later.");
        }
        return ResponseEntity.created(URI.create("")).body(imageMapper.modelToImageGetResource(image));
    }

    @Override
    public ResponseEntity<ImageGetResource> updateById(String imageId, ImagePatchResource imagePatchResource) {
        DocumentModel image = documentDaoService.findOneBy(ImageSpecification.withId(imageId));

        image = imageMapper.patchResourceToModel(imagePatchResource, image);
        image = documentDaoService.save(image);

        return ResponseEntity.ok(imageMapper.modelToImageGetResource(image));
    }

    @Override
    public ResponseEntity<Void> removeById(String imageId) {
        if (documentDaoService.existsBy(ImageSpecification.withId(imageId))) {
            DocumentModel image = documentDaoService.findOneBy(ImageSpecification.withId(imageId));
            String imagePath = storageService.generateDocumentPath(image);
            storageService.deleteFile(imagePath);
            documentDaoService.deleteBy(ImageSpecification.withId(imageId));
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException(
                    ResourceNotFoundExceptionTitleEnum.IMAGE_NOT_FOUND,
                    "No image found with the specified criteria");
        }
    }
}