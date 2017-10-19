package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
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
                new UriTemplate("/browsersession/${browserSessionId}/states");

        private final static UriTemplate checkStateProgressTemplate =
                new UriTemplate("/browsersession-artifacts/${browserSessionArtifactId}/progress");

        private static final int millisToWait = 8000;

        public BrowserSessionApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }

        public BrowserSessionApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment,  HttpClientBuilder httpClientBuilder) {super (authInfo, environment, httpClientBuilder); }

        public void createState(BrowserSessionId browserSessionId, String matchingId, long timeoutMillis) {
            Object empty = Collections.emptyMap();
            Map<String, Object> screenshotConfig = ImmutableMap.of("screenShotConfig", empty);
            Map<String, Object>  params = ImmutableMap.of("optMatchingId", matchingId, "extractionConfig", screenshotConfig);
            ObjectMapper mapper = new ObjectMapper();
            Optional<HttpResponse> r = sendPOST(createStateTemplate, ImmutableMap.of("browserSessionId", browserSessionId.toString()), mapper.valueToTree(params)).getOptHttpResponse();
            try {
                if (!r.isPresent()) {
                    throw new WebmateApiClientException("Could not extract state. Got no response");
                }

                ObjectMapper om = new ObjectMapper();
                String[] artifactIds = om.readValue(r.get().getEntity().getContent(), String[].class);
                /* Wait for each returned artifact in turn */
                long deadline = System.currentTimeMillis() + timeoutMillis;
                for (String artifactId : artifactIds) {
                    while (true) {
                        r = sendGET(checkStateProgressTemplate, ImmutableMap.of("browserSessionArtifactId", artifactId)).getOptHttpResponse();
                        LOG.debug("Checking if artifact " + artifactId + " is complete yet");
                        if (r.isPresent() && EntityUtils.toString(r.get().getEntity()).equals("true")) {
                            break;
                        } else if (System.currentTimeMillis() > deadline) {
                            throw new WebmateApiClientException("State extraction timeout reached.");
                        } else {
                            Thread.sleep(millisToWait);
                        }
                    }
                }
            } catch (InterruptedException e) {
                LOG.warn("Got interrupt while waiting for state extraction to complete, extracted state might be incomplete!");
            } catch (IOException e) {
                LOG.error("Failed to check state extraction progress", e);
            }
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
     * Create a new State for the given BrowserSession with a default timeout of 5 minutes.
     * @param browserSessionId BrowserSession, in which the state should be extracted.
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public void createState(BrowserSessionId browserSessionId, String matchingId) {
        createState(browserSessionId, matchingId, 5*60*1000);
    }

    /**
     * Create a new State for the given BrowserSession.
     * @param browserSessionId BrowserSession, in which the state should be extracted.
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @param timeoutMillis Maximal amount of time to wait for the state extraction to complete in milliseconds.
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public void createState(BrowserSessionId browserSessionId, String matchingId, long timeoutMillis) {
        System.out.println("Creating state with matching id [" + matchingId + "] for browsersession [" + browserSessionId + "]");
        apiClient.createState(browserSessionId, matchingId, timeoutMillis);
    }

}
