package com.testfabrik.webmate.javasdk;

import com.google.common.base.Optional;

/**
 * Contains authentication information of an API user.
 */
public class WebmateAuthInfo {
    public final Optional<String> emailAddress;

    public final String apiKey;

    public WebmateAuthInfo(String emailAddress, String apiKey) {
        this.emailAddress = Optional.of(emailAddress);
        this.apiKey = apiKey;
    }

    public WebmateAuthInfo(String apiKey) {
        this.emailAddress = Optional.absent();
        this.apiKey = apiKey;
    }
}
