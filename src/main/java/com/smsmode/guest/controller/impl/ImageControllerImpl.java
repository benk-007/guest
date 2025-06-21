/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.controller.impl;

import com.smsmode.guest.controller.ImageController;
import com.smsmode.guest.resource.image.ImageGetResource;
import com.smsmode.guest.resource.image.ImagePatchResource;
import com.smsmode.guest.service.IdDocumentImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 19 May 2025</p>
 */
@RestController
@RequiredArgsConstructor
public class ImageControllerImpl implements ImageController {

    private final IdDocumentImageService imageService;

    @Override
    public ResponseEntity<Page<ImageGetResource>> getImageByIdDocumentId(String idDocumentId, Pageable pageable) {
        return imageService.retrieveImages(idDocumentId, pageable);
    }

    @Override
    public ResponseEntity<Resource> getImageById(String imageId) {
        return imageService.retrieveImage(imageId);
    }

    @Override
    public ResponseEntity<ImageGetResource> postImageByIdDocumentId(String idDocumentId, MultipartFile file) {
        return imageService.createImage(idDocumentId, file);
    }

    @Override
    public ResponseEntity<ImageGetResource> patchImageById(String imageId, ImagePatchResource imagePatchResource) {
        return imageService.updateById(imageId, imagePatchResource);
    }

    @Override
    public ResponseEntity<Void> deleteImageById(String imageId) {
        return imageService.removeById(imageId);
    }
}