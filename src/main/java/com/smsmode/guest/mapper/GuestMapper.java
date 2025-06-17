package com.smsmode.guest.mapper;

import com.smsmode.guest.model.GuestModel;
import com.smsmode.guest.model.IdentificationDocumentModel;
import com.smsmode.guest.model.base.AbstractBaseModel;
import com.smsmode.guest.resource.common.AuditGetResource;
import com.smsmode.guest.resource.guest.GuestGetResource;
import com.smsmode.guest.resource.guest.GuestPatchResource;
import com.smsmode.guest.resource.guest.GuestPostResource;
import com.smsmode.guest.resource.iddocument.IdDocumentPostResource;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;

import java.util.List;

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
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {IdentificationDocumentMapper.class})
public abstract class GuestMapper {

    /**
     * Maps GuestPostResource to GuestModel for creation.
     */
    @Mapping(target = "idDocuments", ignore = true)
    public abstract GuestModel postResourceToModel(GuestPostResource guestPostResource);

    /**
     * Maps GuestModel to GuestGetResource for retrieval.
     */
    public abstract GuestGetResource modelToGetResource(GuestModel guestModel);

    /**
     * Maps GuestPatchResource to GuestModel for partial updates.
     */
    @Mapping(target = "idDocuments", ignore = true)
    public abstract GuestModel patchResourceToModel(GuestPatchResource guestPatchResource, @MappingTarget GuestModel guestModel);

    /**
     * Maps AbstractBaseModel to AuditGetResource for audit information.
     */
    public abstract AuditGetResource modelToAuditResource(AbstractBaseModel baseModel);

    /**
     * Maps IdDocumentPostResource to IdentificationDocumentModel.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "guest", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(source = "type", target = "type")
    @Mapping(source = "documentNumber", target = "documentNumber")
    @Mapping(source = "expirationDate", target = "expirationDate")
    public abstract IdentificationDocumentModel idDocumentPostToModel(IdDocumentPostResource idDocumentPostResource);

    /**
     * After mapping method to set audit information and handle ID documents.
     */
    @AfterMapping
    public void afterModelToGetResource(GuestModel guestModel, @MappingTarget GuestGetResource guestGetResource) {
        guestGetResource.setAudit(this.modelToAuditResource(guestModel));
    }

    /**
     * Helper method to handle ID documents mapping during creation.
     */
    @AfterMapping
    public void afterPostResourceToModel(GuestPostResource guestPostResource, @MappingTarget GuestModel guestModel) {
        if (guestPostResource.getIdDocuments() != null) {
            List<IdentificationDocumentModel> idDocuments = guestPostResource.getIdDocuments().stream()
                    .map(this::idDocumentPostToModel)
                    .toList();

            for (IdentificationDocumentModel idDoc : idDocuments) {
                guestModel.addIdDocument(idDoc);
            }
        }
    }

    /**
     * Helper method to handle ID documents mapping during update.
     */
    @AfterMapping
    public void afterPatchResourceToModel(GuestPatchResource guestPatchResource, @MappingTarget GuestModel guestModel) {
        if (guestPatchResource.getIdDocuments() != null) {
            // Clear existing documents
            guestModel.getIdDocuments().clear();

            // Add new documents
            List<IdentificationDocumentModel> idDocuments = guestPatchResource.getIdDocuments().stream()
                    .map(this::idDocumentPostToModel)
                    .toList();

            for (IdentificationDocumentModel idDoc : idDocuments) {
                guestModel.addIdDocument(idDoc);
            }
        }
    }
}