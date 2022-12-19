package com.inov8.integration.exception;

/**
 * Created by inov8 on 8/28/2017.
 */
public class I8SBValidationException extends RuntimeException {
    private static final long serialVersionUID = -2714364496807971467L;

    public I8SBValidationException(String message) {
        super(message);
    }

    public I8SBValidationException(Throwable throwable) {
        super(throwable);
    }

    public I8SBValidationException(Throwable throwable, String message) {
        super(message, throwable);
    }
}
