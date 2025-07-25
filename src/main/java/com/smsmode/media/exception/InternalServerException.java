/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.media.exception;

import com.smsmode.media.exception.enumeration.BaseExceptionEnum;
import org.springframework.http.HttpStatus;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 20 May 2025</p>
 */
public class InternalServerException extends AbstractBaseException {
    /**
     * Constructs an instance of {@code AbstractBaseException} with the specified title, status, and
     * message.
     *
     * @param title   The title or type of the exception, represented by a {@link BaseExceptionEnum}.
     * @param message The detail message (which is saved for later retrieval by the {@link
     *                #getMessage()} method).
     */
    public InternalServerException(BaseExceptionEnum title, String message) {
        super(title, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
