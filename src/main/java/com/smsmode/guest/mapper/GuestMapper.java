package com.smsmode.guest.mapper;

import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.model.base.AbstractBaseModel;
import com.smsmode.guest.resource.common.AuditGetResource;
import com.smsmode.guest.resource.guest.GuestGetResource;
import com.smsmode.guest.resource.guest.GuestPatchResource;
import com.smsmode.guest.resource.guest.GuestPostResource;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;

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

    /**
     * Maps GuestPostResource to GuestModel for creation.
     */
    public abstract GuestModel postResourceToModel(GuestPostResource guestPostResource);

    /**
     * Maps GuestModel to GuestGetResource for retrieval.
     */
    public abstract GuestGetResource modelToGetResource(GuestModel guestModel);

    /**
     * Maps GuestPatchResource to GuestModel for partial updates.
     */
    public abstract GuestModel patchResourceToModel(GuestPatchResource guestPatchResource, @MappingTarget GuestModel guestModel);

    /**
     * Maps AbstractBaseModel to AuditGetResource for audit information.
     */
    public abstract AuditGetResource modelToAuditResource(AbstractBaseModel baseModel);

    /**
     * After mapping method to set audit information.
     */
    @AfterMapping
    public void afterModelToGetResource(GuestModel guestModel, @MappingTarget GuestGetResource guestGetResource) {
        guestGetResource.setAudit(this.modelToAuditResource(guestModel));
    }
}