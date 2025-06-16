package com.smsmode.guest.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Embeddable class representing an address for a guest.
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 16 Jun 2025</p>
 */
@Getter
@Setter
@Embeddable
public class AddressEmbeddable {

    @Column(name = "STREET1")
    private String street1;

    @Column(name = "STREET2")
    private String street2;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "CITY")
    private String city;

    @Column(name = "COUNTRY")
    private String country;
}