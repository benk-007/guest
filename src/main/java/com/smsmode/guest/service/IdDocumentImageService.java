/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service;

import com.smsmode.guest.resource.image.ImageGetResource;
import com.smsmode.guest.resource.image.ImagePatchResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 19 May 2025</p>
 */
public interface IdDocumentImageService {

    ResponseEntity<Page<ImageGetResource>> retrieveImages(String idDocumentId, Pageable pageable);
    ResponseEntity<Resource> retrieveImage(String imageId);
    ResponseEntity<ImageGetResource> createImage(String idDocumentId, MultipartFile file);

    ResponseEntity<Void> removeById(String imageId);

    ResponseEntity<ImageGetResource> updateById(String imageId, ImagePatchResource imagePatchResource);

}
