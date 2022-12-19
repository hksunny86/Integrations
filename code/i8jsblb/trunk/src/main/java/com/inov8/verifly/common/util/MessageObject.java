package com.inov8.verifly.common.util;

public class MessageObject {
    private boolean error;
    private String message;
    private long errorCode;

    public String getMessage() {
        return message;
    }

    public long getErrorCode() {
        return errorCode;
    }

    public boolean isError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
