/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.mapper;

import com.smsmode.guest.model.IdentificationDocumentModel;
import com.smsmode.guest.model.base.AbstractBaseModel;
import com.smsmode.guest.resource.common.AuditGetResource;
import com.smsmode.guest.resource.iddocument.IdDocumentGetResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPatchResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPostResource;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;

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
public abstract class IdentificationDocumentMapper {

    /**
     * Maps IdDocumentPostResource to IdentificationDocumentModel for creation.
     */
    public abstract IdentificationDocumentModel postResourceToModel(IdDocumentPostResource idDocumentPostResource);

    /**
     * Maps IdentificationDocumentModel to IdDocumentGetResource for retrieval.
     */
    public abstract IdDocumentGetResource modelToGetResource(IdentificationDocumentModel idDocumentModel);

    /**
     * Maps IdDocumentPatchResource to IdentificationDocumentModel for partial updates.
     */
    public abstract IdentificationDocumentModel patchResourceToModel(IdDocumentPatchResource idDocumentPatchResource, @MappingTarget IdentificationDocumentModel idDocumentModel);

    /**
     * Maps AbstractBaseModel to AuditGetResource for audit information.
     */
    public abstract AuditGetResource modelToAuditResource(AbstractBaseModel baseModel);

    /**
     * After mapping method to set audit information.
     */
    @AfterMapping
    public void afterModelToGetResource(IdentificationDocumentModel idDocumentModel, @MappingTarget IdDocumentGetResource idDocumentGetResource) {
        idDocumentGetResource.setAudit(this.modelToAuditResource(idDocumentModel));
    }
}