package com.testfabrik.webmate.javasdk;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * The WebmateEnvironment contains important parameters for accessing webmate, e.g. the API base URI.
 */
public class WebmateEnvironment {

    private final static String WEBMATE_DEFAULT_BASEURI = "https://api.webmate.io/v1";
//    private final static String WEBMATE_DEFAULT_BASEURI = "http://localhost:54321/v1";

    public final URI baseURI;

    public static WebmateEnvironment create() {
        URI baseUri;
        try {
           baseUri = new URI(WEBMATE_DEFAULT_BASEURI);
        } catch (URISyntaxException e) {
            throw new WebmateApiClientException("Could not create webmate Environment. Invalid URL.", e);
        }
        return new WebmateEnvironment(baseUri);
    }

    public static WebmateEnvironment create(URI baseUri) {
        return new WebmateEnvironment(baseUri);
    }

    /**
     * Create WebmateEnvironment with new base URI.
     * @param baseURI Base URI to webmate API
     */
    protected WebmateEnvironment(URI baseURI) {
        this.baseURI = baseURI;
    }
}
