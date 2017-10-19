package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.WebmateAPISession;
import com.testfabrik.webmate.javasdk.WebmateApiClient;
import com.testfabrik.webmate.javasdk.WebmateAuthInfo;
import com.testfabrik.webmate.javasdk.WebmateEnvironment;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

/**
 * Facade for webmate BrowserSession service.
 */
public class BrowserSessionClient {

    private WebmateAPISession session;
    private BrowserSessionApiClient apiClient;

    private static final Logger LOG = LoggerFactory.getLogger(BrowserSessionClient.class);

    private static class BrowserSessionApiClient extends WebmateApiClient {

        private final static UriTemplate createStateTemplate =
                new UriTemplate("/browsersession/${browserSessionId}/states", ImmutableMap.of("withScreenshot", "true"));

        public BrowserSessionApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }

        public BrowserSessionApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment,  HttpClientBuilder httpClientBuilder) {super (authInfo, environment, httpClientBuilder); }

        public void createState(BrowserSessionId browserSessionId, String matchingId) {
            Map<String, String>  params = ImmutableMap.of("matchingId", matchingId);
            ObjectMapper mapper = new ObjectMapper();
            sendPOST(createStateTemplate, ImmutableMap.of("browserSessionId", browserSessionId.toString()), mapper.valueToTree(params));
        }

    }

    public BrowserSessionClient(WebmateAPISession session) {
        this.session = session;
        this.apiClient = new BrowserSessionApiClient(session.authInfo, session.environment);
    }

    public BrowserSessionClient(WebmateAPISession session,  HttpClientBuilder httpClientBuilder) {
        this.session = session;
        this.apiClient = new BrowserSessionApiClient(session.authInfo, session.environment, httpClientBuilder);
    }



    /**
     * Return the webmate BrowserSessionId for a given Selenium session running in webmate.
     * @param opaqueSeleniumSessionIdString Selenium SessionId that can be obtained by calling WebDriver.getSessionId().toString().
     * @return BrowserSessionId
     */
    public BrowserSessionRef getBrowserSessionForSeleniumSession(String opaqueSeleniumSessionIdString) {
        // it seems that currently the BrowserSessionId is equal to the Selenium SessionId (which I would consider a bug)
        return new BrowserSessionRef(new BrowserSessionId(UUID.fromString(opaqueSeleniumSessionIdString)), session);
    }

    /**
     * Create a new State for the given BrowserSession.
     * @param browserSessionId BrowserSession, in which the state should be extracted.
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     */
    public void createState(BrowserSessionId browserSessionId, String matchingId) {

        System.out.println("Creating state with matching id [" + matchingId + "] for browsersession [" + browserSessionId + "]");
        apiClient.createState(browserSessionId, matchingId);
    }

}
