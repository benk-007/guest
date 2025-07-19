package com.smsmode.media.mapper;

import com.smsmode.media.dao.service.IdentityDocumentDaoService;
import com.smsmode.media.dao.specification.IdentityDocumentSpecification;
import com.smsmode.media.model.GuestModel;
import com.smsmode.media.model.base.AbstractBaseModel;
import com.smsmode.media.resource.common.AuditGetResource;
import com.smsmode.media.resource.guest.GuestItemGetResource;
import com.smsmode.media.resource.guest.GuestPatchResource;
import com.smsmode.media.resource.guest.GuestPostResource;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Mapper for Guest entity and resources.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@Slf4j
@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class GuestMapper {

    private IdentityDocumentDaoService identityDocumentDaoService;

    @Autowired
    void setIdentityDocumentDaoService(IdentityDocumentDaoService identityDocumentDaoService) {
        this.identityDocumentDaoService = identityDocumentDaoService;
    }

    /**
     * Maps GuestPostResource to GuestModel for creation.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    public abstract GuestModel postResourceToModel(GuestPostResource guestPostResource);

    /**
     * Maps GuestModel to GuestGetResource for retrieval.
     */
    public abstract GuestItemGetResource modelToGetResource(GuestModel guestModel);

    /**
     * Maps GuestPatchResource to GuestModel for partial updates.
     */
    public abstract GuestModel patchResourceToModel(GuestPatchResource guestPatchResource, @MappingTarget GuestModel guestModel);

    /**
     * Maps AbstractBaseModel to AuditGetResource for audit information.
     */
    public abstract AuditGetResource modelToAuditResource(AbstractBaseModel baseModel);

    /**
     * After mapping method to set audit information and handle ID documents.
     */
    @AfterMapping
    public void afterModelToItemGetResource(GuestModel guestModel, @MappingTarget GuestItemGetResource guestItemGetResource) {
        guestItemGetResource.setAudit(this.modelToAuditResource(guestModel));
        guestItemGetResource.setWithIdentityDocument(identityDocumentDaoService.existsBy(IdentityDocumentSpecification.withFile().and(IdentityDocumentSpecification.withGuestId(guestItemGetResource.getId()))));
    }
}