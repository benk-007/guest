/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.controller;

import com.smsmode.guest.resource.image.ImageGetResource;
import com.smsmode.guest.resource.image.ImagePatchResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 19 May 2025</p>
 */
@RequestMapping("/images")
public interface ImageController {

    @GetMapping
    ResponseEntity<Page<ImageGetResource>> getImageByIdDocumentId(@RequestParam("idDocumentId") String idDocumentId, Pageable pageable);

    @GetMapping("/{imageId}")
    ResponseEntity<Resource> getImageById(@PathVariable String imageId);

    @PostMapping
    ResponseEntity<ImageGetResource> postImageByIdDocumentId(@RequestParam("idDocumentId") String idDocumentId, @RequestParam("file") MultipartFile file);

    @PatchMapping("/{imageId}")
    ResponseEntity<ImageGetResource> patchImageById(@PathVariable String imageId, @RequestBody ImagePatchResource imagePatchResource);

    @DeleteMapping("/{imageId}")
    ResponseEntity<Void> deleteImageById(@PathVariable String imageId);
}