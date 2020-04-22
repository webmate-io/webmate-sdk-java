package com.testfabrik.webmate.javasdk.commonutils;

import com.testfabrik.webmate.javasdk.WebmateApiClient;
import com.testfabrik.webmate.javasdk.WebmateApiClientException;
import com.testfabrik.webmate.javasdk.WebmateAuthInfo;
import com.testfabrik.webmate.javasdk.WebmateEnvironment;
import org.apache.http.HttpResponse;

import java.util.HashMap;

public class ConnectionTestingClient extends WebmateApiClient {

    private static final UriTemplate currentUserTemplate = new UriTemplate("/users/current");

    /**
     * Simple client to test the connectivity for the given credentials and environment
     *
     * @param authInfo an instance of WebmateAuthInfo which contains the users credentials
     * @param environment an instance of WebmateEnvironment which contains the url of webmate
     */
    public ConnectionTestingClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
        super(authInfo, environment);
    }

    /**
     * Tests the connection to webmate given the configuration passed to the constructor.
     *
     * @return a Result enum value presenting the connectivity state
     */
    public Result testConnection() {
        try {
            HttpResponse r = sendGETUnchecked(currentUserTemplate, new HashMap<String, String>());
            switch (r.getStatusLine().getStatusCode()) {
                case 200:
                    return Result.SUCCESS;
                case 403:
                    return Result.AUTH_FAILURE;
                case 404:
                    return Result.UNREACHABLE;
                default:
                    return Result.UNREACHABLE;
            }
        } catch (WebmateApiClientException e) {
            return Result.UNREACHABLE;
        }
    }

    public static enum Result {
        SUCCESS, UNREACHABLE, AUTH_FAILURE
    }
}
