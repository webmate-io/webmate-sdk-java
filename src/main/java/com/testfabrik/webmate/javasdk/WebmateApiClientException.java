package com.testfabrik.webmate.javasdk;

/**
 * Generic Exception of webmate API client.
 */
public class WebmateApiClientException extends RuntimeException {
    private final Integer statusCode;

    public WebmateApiClientException(String errorMsg) {
        super(errorMsg);
        this.statusCode = null;
    }

    public WebmateApiClientException(String errorMsg, Throwable e) {
        super(errorMsg, e);
        this.statusCode = null;
    }

    public WebmateApiClientException(String errorMsg, int statusCode) {
        super(errorMsg);
        this.statusCode = statusCode;
    }

    public WebmateApiClientException(String errorMsg, int statusCode, Throwable e) {
        super(errorMsg, e);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
