/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service.impl;

import com.smsmode.guest.dao.service.ImageDaoService;
import com.smsmode.guest.dao.service.IdentificationDocumentDaoService;
import com.smsmode.guest.dao.specification.ImageSpecification;
import com.smsmode.guest.dao.specification.IdentificationDocumentSpecification;
import com.smsmode.guest.exception.InternalServerException;
import com.smsmode.guest.exception.ResourceNotFoundException;
import com.smsmode.guest.exception.enumeration.InternalServerExceptionTitleEnum;
import com.smsmode.guest.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.smsmode.guest.mapper.ImageMapper;
import com.smsmode.guest.model.IdentificationDocumentModel;
import com.smsmode.guest.model.ImageModel;
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

    private final IdentificationDocumentDaoService identificationDocumentDaoService;
    private final ImageDaoService imageDaoService;
    private final StorageService storageService;
    private final ImageMapper imageMapper;

    @Override
    public ResponseEntity<Page<ImageGetResource>> retrieveImages(String idDocumentId, Pageable pageable) {
        Page<ImageModel> imageModels = imageDaoService.findAllBy(ImageSpecification.withIdDocumentId(idDocumentId), pageable);
        return ResponseEntity.ok(imageModels.map(imageMapper::modelToImageGetResource));
    }

    @Override
    public ResponseEntity<Resource> retrieveImage(String imageId) {

        ImageModel image = imageDaoService.findOneBy(ImageSpecification.withId(imageId));

        String imagePath = storageService.generateIdDocumentImagePath(image);
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

        IdentificationDocumentModel idDocument = identificationDocumentDaoService.findOneBy(IdentificationDocumentSpecification.withId(idDocumentId));

        ImageModel image = new ImageModel();
        image.setFileName(file.getOriginalFilename());
        image.setIdDocument(idDocument);
        if (!imageDaoService.existsBy(ImageSpecification.withIdDocumentId(idDocumentId))) {
            image.setCover(true);
        }
        image = imageDaoService.save(image);

        String imagePath = storageService.generateIdDocumentImagePath(image);

        try {
            String imageFileName = storageService.storeFile(imagePath, file.getInputStream());
            if (ObjectUtils.isEmpty(imageFileName)) {
                imageDaoService.deleteBy(ImageSpecification.withId(image.getId()));
                throw new InternalServerException(InternalServerExceptionTitleEnum.FILE_UPLOAD, "An unexpected error occurred while saving the image. Please try again later.");
            }
        } catch (IOException e) {
            log.warn("An error occurred when storing image file", e);
            imageDaoService.deleteBy(ImageSpecification.withId(image.getId()));
            throw new InternalServerException(InternalServerExceptionTitleEnum.FILE_UPLOAD, "An unexpected error occurred while saving the image. Please try again later.");
        }
        return ResponseEntity.created(URI.create("")).body(imageMapper.modelToImageGetResource(image));
    }

    @Override
    public ResponseEntity<ImageGetResource> updateById(String imageId, ImagePatchResource imagePatchResource) {
        ImageModel image = imageDaoService.findOneBy(ImageSpecification.withId(imageId));

        if (imagePatchResource.isCover() && imageDaoService.existsBy(ImageSpecification.withCover(true))) {
            ImageModel coverImage = imageDaoService.findOneBy(ImageSpecification.withCover(true));
            coverImage.setCover(false);
            imageDaoService.save(coverImage);
        }

        image = imageMapper.patchResourceToModel(imagePatchResource, image);
        image = imageDaoService.save(image);

        return ResponseEntity.ok(imageMapper.modelToImageGetResource(image));
    }

    @Override
    public ResponseEntity<Void> removeById(String imageId) {
        if (imageDaoService.existsBy(ImageSpecification.withId(imageId))) {
            ImageModel image = imageDaoService.findOneBy(ImageSpecification.withId(imageId));
            String imagePath = storageService.generateIdDocumentImagePath(image);
            storageService.deleteFile(imagePath);
            imageDaoService.deleteBy(ImageSpecification.withId(imageId));
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException(
                    ResourceNotFoundExceptionTitleEnum.IMAGE_NOT_FOUND,
                    "No image found with the specified criteria");
        }
    }
}