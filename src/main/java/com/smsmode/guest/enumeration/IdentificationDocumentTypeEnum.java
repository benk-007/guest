package com.smsmode.guest.enumeration;

public enum IdentificationDocumentTypeEnum {
    IDENTITY_CARD("Identity Card"),
    PASSPORT("Passport"),
    DRIVER_LICENCE("Driver Licence");

    private final String displayName;

    IdentificationDocumentTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
