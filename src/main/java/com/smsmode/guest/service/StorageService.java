/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service;

import com.smsmode.guest.model.ImageModel;

import java.io.InputStream;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 28 Apr 2025</p>
 */
public interface StorageService {

    String storeFile(String path, InputStream inputStream);

    void deleteFile(String path);

    String generateIdDocumentImagePath(ImageModel image);
}