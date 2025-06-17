/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service.impl;

import com.smsmode.guest.dao.service.GuestDaoService;
import com.smsmode.guest.dao.service.IdentificationDocumentDaoService;
import com.smsmode.guest.dao.specification.GuestSpecification;
import com.smsmode.guest.dao.specification.IdentificationDocumentSpecification;
import com.smsmode.guest.exception.ResourceNotFoundException;
import com.smsmode.guest.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.smsmode.guest.mapper.IdentificationDocumentMapper;
import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.model.IdentificationDocumentModel;
import com.smsmode.guest.resource.iddocument.IdDocumentGetResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPostResource;
import com.smsmode.guest.service.IdentificationDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
public class IdentificationDocumentServiceImpl implements IdentificationDocumentService {

    private final IdentificationDocumentDaoService identificationDocumentDaoService;
    private final IdentificationDocumentMapper identificationDocumentMapper;
    private final GuestDaoService guestDaoService;

    @Override
    public ResponseEntity<IdDocumentGetResource> create(String guestId, IdDocumentPostResource idDocumentPostResource) {

        GuestModel guest = guestDaoService.findOneBy(GuestSpecification.withIdEqual(guestId));

        IdentificationDocumentModel idDocumentModel = identificationDocumentMapper.postResourceToModel(idDocumentPostResource);
        idDocumentModel.setGuest(guest);
        idDocumentModel = identificationDocumentDaoService.save(idDocumentModel);

        return ResponseEntity.created(URI.create("")).body(identificationDocumentMapper.modelToGetResource(idDocumentModel));
    }

    @Override
    public ResponseEntity<Page<IdDocumentGetResource>> retrieveAllByGuestId(String guestId, Pageable pageable) {

        if (!guestDaoService.existsById(guestId)) {
            throw new ResourceNotFoundException(
                    ResourceNotFoundExceptionTitleEnum.GUEST_NOT_FOUND,
                    "Guest not found with ID: " + guestId);
        }

        Specification<IdentificationDocumentModel> spec = IdentificationDocumentSpecification.withGuestId(guestId);
        Page<IdentificationDocumentModel> idDocuments = identificationDocumentDaoService.findAllBy(spec, pageable);

        return ResponseEntity.ok(idDocuments.map(identificationDocumentMapper::modelToGetResource));
    }

    @Override
    public ResponseEntity<IdDocumentGetResource> retrieveById(String guestId, String idDocumentId) {

        Specification<IdentificationDocumentModel> spec = Specification.where(
                        IdentificationDocumentSpecification.withId(idDocumentId))
                .and(IdentificationDocumentSpecification.withGuestId(guestId));

        IdentificationDocumentModel idDocument = identificationDocumentDaoService.findOneBy(spec);

        return ResponseEntity.ok(identificationDocumentMapper.modelToGetResource(idDocument));
    }

    @Override
    public ResponseEntity<IdDocumentGetResource> updateById(String guestId, String idDocumentId, IdDocumentPatchResource idDocumentPatchResource) {

        Specification<IdentificationDocumentModel> spec = Specification.where(
                        IdentificationDocumentSpecification.withId(idDocumentId))
                .and(IdentificationDocumentSpecification.withGuestId(guestId));

        IdentificationDocumentModel existingIdDocument = identificationDocumentDaoService.findOneBy(spec);
        IdentificationDocumentModel updatedIdDocument = identificationDocumentMapper.patchResourceToModel(idDocumentPatchResource, existingIdDocument);
        updatedIdDocument = identificationDocumentDaoService.save(updatedIdDocument);

        return ResponseEntity.ok(identificationDocumentMapper.modelToGetResource(updatedIdDocument));
    }

    @Override
    public ResponseEntity<Void> deleteById(String guestId, String idDocumentId) {

        Specification<IdentificationDocumentModel> spec = Specification.where(
                        IdentificationDocumentSpecification.withId(idDocumentId))
                .and(IdentificationDocumentSpecification.withGuestId(guestId));

        if (identificationDocumentDaoService.existsBy(spec)) {
            identificationDocumentDaoService.deleteBy(spec);
            return ResponseEntity.noContent().build();
        }

        throw new ResourceNotFoundException(
                ResourceNotFoundExceptionTitleEnum.ID_DOCUMENT_NOT_FOUND,
                "Identification document not found with ID: " + idDocumentId);
    }
}