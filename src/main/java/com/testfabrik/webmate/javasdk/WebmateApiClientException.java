package com.testfabrik.webmate.javasdk;

/**
 * Generic Exception of webmate API client.
 */
public class WebmateApiClientException extends RuntimeException {
    public WebmateApiClientException(String errorMsg) {
        super(errorMsg);
    }

    public WebmateApiClientException(String errorMsg, Throwable e) {
        super(errorMsg, e);
    }
}
