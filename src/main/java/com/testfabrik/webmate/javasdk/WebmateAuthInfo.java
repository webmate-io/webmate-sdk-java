package com.testfabrik.webmate.javasdk;

/**
 * Contains authentication information of an API user.
 */
public class WebmateAuthInfo {
    public final String emailAddress;

    public final String apiKey;

    public WebmateAuthInfo(String emailAddress, String apiKey) {
        this.emailAddress = emailAddress;
        this.apiKey = apiKey;
    }
}
