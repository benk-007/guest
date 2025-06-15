/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.model;

import com.smsmode.guest.embeddable.AddressEmbeddable;
import com.smsmode.guest.embeddable.ContactEmbeddable;
import com.smsmode.guest.embeddable.OccupancyEmbeddable;
import com.smsmode.guest.enumeration.AmenityEnum;
import com.smsmode.guest.enumeration.FloorSizeUnitEnum;
import com.smsmode.guest.enumeration.UnitNatureEnum;
import com.smsmode.guest.enumeration.UnitTypeEnum;
import com.smsmode.guest.enumeration.converter.AmenityEnumSetToStringConverter;
import com.smsmode.guest.model.base.AbstractBaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 11 Apr 2025</p>
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "X_GUEST")
public class GuestModel extends AbstractBaseModel {


}
