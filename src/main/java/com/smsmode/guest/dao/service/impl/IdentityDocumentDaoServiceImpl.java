package com.smsmode.guest.dao.service.impl;

import com.smsmode.guest.dao.repository.IdentificationDocumentRepository;
import com.smsmode.guest.dao.service.IdentityDocumentDaoService;
import com.smsmode.guest.exception.ResourceNotFoundException;
import com.smsmode.guest.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.smsmode.guest.model.IdentityDocumentModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Implementation of IdentificationDocumentDaoService.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityDocumentDaoServiceImpl implements IdentityDocumentDaoService {

    private final IdentificationDocumentRepository idDocumentRepository;

    @Override
    public IdentityDocumentModel save(IdentityDocumentModel idDocument) {
        return idDocumentRepository.save(idDocument);
    }

    @Override
    public IdentityDocumentModel findOneBy(Specification<IdentityDocumentModel> specification) {
        return idDocumentRepository.findOne(specification).orElseThrow(
                () -> {
                    log.debug("Couldn't find any identification document with the specified criteria");
                    return new ResourceNotFoundException(
                            ResourceNotFoundExceptionTitleEnum.ID_DOCUMENT_NOT_FOUND,
                            "No identification document found with the specified criteria");
                });
    }

    @Override
    public Page<IdentityDocumentModel> findAllBy(Specification<IdentityDocumentModel> specification, Pageable pageable) {
        return idDocumentRepository.findAll(specification, pageable);
    }

    @Override
    public boolean existsBy(Specification<IdentityDocumentModel> specification) {
        return idDocumentRepository.exists(specification);
    }

    @Override
    public void deleteBy(Specification<IdentityDocumentModel> specification) {
        idDocumentRepository.delete(specification);
    }
}