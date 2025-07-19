/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.media.dao.service.impl;

import com.smsmode.media.dao.repository.DocumentRepository;
import com.smsmode.media.dao.service.DocumentDaoService;
import com.smsmode.media.exception.ResourceNotFoundException;
import com.smsmode.media.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.smsmode.media.model.DocumentModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 19 May 2025</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentDaoServiceImpl implements DocumentDaoService {

    private final DocumentRepository documentRepository;

    @Override
    public boolean existsBy(Specification<DocumentModel> specification) {
        return documentRepository.exists(specification);
    }

    @Override
    public DocumentModel save(DocumentModel image) {
        return documentRepository.save(image);
    }

    @Override
    public void deleteBy(Specification<DocumentModel> specification) {
        documentRepository.delete(specification);
    }

    @Override
    public Page<DocumentModel> findAllBy(Specification<DocumentModel> specification, Pageable pageable) {
        return documentRepository.findAll(specification, pageable);
    }

    @Override
    public DocumentModel findOneBy(Specification<DocumentModel> specification) {
        return documentRepository.findOne(specification).orElseThrow(
                () -> {
                    log.debug("Couldn't find any image with the specified criteria");
                    return new ResourceNotFoundException(
                            ResourceNotFoundExceptionTitleEnum.IMAGE_NOT_FOUND,
                            "No image found with the specified criteria");
                });
    }
}
