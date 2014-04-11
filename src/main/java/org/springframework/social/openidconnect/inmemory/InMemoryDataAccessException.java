package org.springframework.social.openidconnect.inmemory;

import org.springframework.social.ApiException;

/**
 * Exception for InMemory Data Access operations.
 */
public class InMemoryDataAccessException extends ApiException {

    /**
     * InMemoryDataException Exception
     * @param message  - Exception message
     * @param exception - Exception itself.
     */
    public InMemoryDataAccessException(String message, Exception exception){
        super("paypal",message, exception);
    }
}
