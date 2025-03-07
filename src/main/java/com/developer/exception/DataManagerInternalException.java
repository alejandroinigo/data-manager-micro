package com.developer.exception;

import lombok.Getter;

@Getter
public class DataManagerInternalException extends RuntimeException {

    public DataManagerInternalException() {
        super();
    }

    public DataManagerInternalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DataManagerInternalException(final String message) {
        super(message);
    }

    public DataManagerInternalException(final Throwable cause) {
        super(cause);
    }
}
