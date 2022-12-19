package com.inov8.integration.exception;

/**
 * Created by inov8 on 8/28/2017.
 */
public class I8SBServiceNotAvailableException extends RuntimeException {


    private static final long serialVersionUID = -1507891019208760266L;

    public I8SBServiceNotAvailableException(String message) {
        super(message);
    }

    public I8SBServiceNotAvailableException(Throwable throwable) {
        super(throwable);
    }

    public I8SBServiceNotAvailableException(Throwable throwable, String message) {
        super(message, throwable);
    }
}
