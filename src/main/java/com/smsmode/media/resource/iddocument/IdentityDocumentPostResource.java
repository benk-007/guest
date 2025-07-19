package com.smsmode.media.resource.iddocument;

import com.smsmode.media.enumeration.IdentityDocumentTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IdentityDocumentPostResource {
    @NotNull
    private IdentityDocumentTypeEnum type;
    @NotBlank
    private String value;
    @NotNull
    private LocalDate expirationDate;
    @NotBlank
    private String guestId;
}
