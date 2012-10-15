package org.springframework.social.openidconnect;

import org.springframework.social.ApiException;

/**
 * Exception for PayPal Access operations.  Wraps up all exception as Spring social APIException.
 */
public class PayPalAccessException extends ApiException {

    /**
     * PayPal Exception
     * @param message  - Exception message
     * @param exception - Exception itself.
     */
    public PayPalAccessException(String message, Exception exception){
        super(message, exception);
    }
}
