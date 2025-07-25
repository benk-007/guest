/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.media.mapper;

import com.smsmode.media.model.IdentityDocumentModel;
import com.smsmode.media.model.base.AbstractBaseModel;
import com.smsmode.media.resource.common.AuditGetResource;
import com.smsmode.media.resource.guest.GuestIdDocumentPostResource;
import com.smsmode.media.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.media.resource.iddocument.IdentityDocumentItemGetResource;
import com.smsmode.media.resource.iddocument.IdentityDocumentPostResource;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

/**
 * Mapper for IdentificationDocument entity and resources.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@Slf4j
@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class IdentityDocumentMapper {

    /**
     * Maps IdDocumentPostResource to IdentificationDocumentModel for creation.
     */
    @Mapping(target = "guest", ignore = true)
    public abstract IdentityDocumentModel postResourceToModel(IdentityDocumentPostResource identityDocumentPostResource);

    @Mapping(target = "guest", ignore = true)
    public abstract IdentityDocumentModel postResourceToModel(GuestIdDocumentPostResource identityDocumentPostResource);

    /**
     * Maps IdentificationDocumentModel to IdDocumentGetResource for retrieval.
     */
    public abstract IdentityDocumentItemGetResource modelToItemGetResource(IdentityDocumentModel identityDocumentModel);

    /**
     * Maps IdDocumentPatchResource to IdentificationDocumentModel for partial updates.
     */
    @Mapping(target = "guest", ignore = true)
    public abstract IdentityDocumentModel patchResourceToModel(IdDocumentPatchResource idDocumentPatchResource, @MappingTarget IdentityDocumentModel idDocumentModel);

    /**
     * Maps AbstractBaseModel to AuditGetResource for audit information.
     */
    public abstract AuditGetResource modelToAuditResource(AbstractBaseModel baseModel);

    /**
     * After mapping method to set audit information.
     */
    @AfterMapping
    public void afterModelToItemGetResource(IdentityDocumentModel identityDocumentModel, @MappingTarget IdentityDocumentItemGetResource identityDocumentItemGetResource) {
        identityDocumentItemGetResource.setFileProvided(!ObjectUtils.isEmpty(identityDocumentModel));
        identityDocumentItemGetResource.setAudit(this.modelToAuditResource(identityDocumentModel));
    }
}