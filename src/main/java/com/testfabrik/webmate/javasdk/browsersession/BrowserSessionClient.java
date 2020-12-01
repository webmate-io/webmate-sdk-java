package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import com.testfabrik.webmate.javasdk.commonutils.HttpHelpers;
import com.testfabrik.webmate.javasdk.testmgmt.Artifact;
import com.testfabrik.webmate.javasdk.testmgmt.ArtifactId;
import com.testfabrik.webmate.javasdk.testmgmt.TestRunId;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Facade for webmate BrowserSession service.
 */
public class BrowserSessionClient {

    private WebmateAPISession session;
    private BrowserSessionApiClient apiClient;

    private Stack<ActionSpanId> currentSpanIds = new Stack<>();

    private static final Logger LOG = LoggerFactory.getLogger(BrowserSessionClient.class);

    private static final Integer DefaultBrowserSessionTimeoutMillis = 5 * 60 * 1000; // Default timeout: 5 minutes

    // Sane default Config, extracting DOM and taking non fullpage Screenshots. Otherwise take Webmate Defaults.
    private static final BrowserSessionStateExtractionConfig DefaultStateExtractionConfig = new BrowserSessionStateExtractionConfig(null, null, null, null, null, true,
                                                                                                                                      new BrowserSessionScreenshotExtractionConfig(false, false), null);

    private static class BrowserSessionApiClient extends WebmateApiClient {

        private final static UriTemplate createStateTemplate =
                new UriTemplate("/browsersession/${browserSessionId}/states");

        private final static UriTemplate checkStateProgressTemplate =
                new UriTemplate("/browsersession/${browserSessionId}/artifacts/${browserSessionArtifactId}/progress");

        private final static UriTemplate addArtifactTemplate =
                new UriTemplate("/browsersession/${expeditionId}/artifacts");

        private final static UriTemplate terminateBrowsersessionTemplate =
                new UriTemplate("/browsersession/${browserSessionId}");

        private static final int millisToWait = 8000;


        /**
         * Creates an webmate api client.
         *
         * @param authInfo The authentication information needed for the API interaction
         * @param environment The environment the client should be used in, i.e which urls to use for communication
         */
        public BrowserSessionApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }


        /**
         * Creates an webmate api client using a custom HttpClientBuilder, which allows the use of proxies.
         *
         * @param authInfo The authentication information needed for the API interaction
         * @param environment The environment the client should be used in, i.e which urls to use for communication
         * @param httpClientBuilder The HttpClientBuilder that is used for the underlying connection.
         */
        public BrowserSessionApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder httpClientBuilder) {super (authInfo, environment, httpClientBuilder); }

        /**
         * Creates a State for a Browsersession with a matching id. The extraction parameters are set to default.
         *
         * @param browserSessionId The Browsersession Id the state should be created for
         * @param matchingId The Id for the state. Used for matching
         * @param timeoutMillis The timeout for the statecreation in milliseconds
         */
        public BrowserSessionStateId createState(BrowserSessionId browserSessionId, String matchingId, long timeoutMillis) {
            Map<String, Object>  params = ImmutableMap.of("optMatchingId", matchingId, "extractionConfig", DefaultStateExtractionConfig);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Optional<HttpResponse> optHttpResponse = sendPOST(createStateTemplate, ImmutableMap.of("browserSessionId", browserSessionId.toString()), mapper.valueToTree(params)).getOptHttpResponse();
            return HttpHelpers.getObjectFromJsonEntity(optHttpResponse.get(), BrowserSessionStateId.class);
        }

        /**
         * Tries to terminate a Browsersession. Will return whether the process was successful or not.
         *
         * @param browserSessionId The id of the session that should be terminated
         * @return true, if the Browersession was terminated successfully.
         */
        public boolean terminateSession(BrowserSessionId browserSessionId) {
            Optional<HttpResponse> r = sendPOST(terminateBrowsersessionTemplate, ImmutableMap.of("browserSessionId", browserSessionId.toString()), "terminate").getOptHttpResponse();
            boolean stopped = false;
            if (!r.isPresent()){
                throw new WebmateApiClientException("Could not stop Browsersession. Got no response");
            }
            else {
                try {
                    stopped = EntityUtils.toString(r.get().getEntity()).equals("true");
                } catch (IOException e) {
                    LOG.error("Failed to read response:", e);
                }
            }
            return stopped;
        }

        /**
         * Create new state artifact associated with browser session.
         *
         * @param browserSessionId id of browser session
         * @param matchingId "Name" of state
         * @param timeoutMillis time to wait for completion of operation (may still be successful after timeout)
         * @param browserSessionStateExtractionConfig configuration controlling the state extraction process. See {@link BrowserSessionScreenshotExtractionConfig}.
         */
        public void createState(BrowserSessionId browserSessionId, String matchingId, long timeoutMillis, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
            Map<String, Object>  params = ImmutableMap.of("optMatchingId", matchingId, "extractionConfig", browserSessionStateExtractionConfig);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            sendPOST(createStateTemplate, ImmutableMap.of("browserSessionId", browserSessionId.toString()), mapper.valueToTree(params)).getOptHttpResponse();
        }

        public void startAction(BrowserSessionId expeditionId, StartStoryActionAddArtifactData art) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String>  params = ImmutableMap.of("expeditionId", expeditionId.getValueAsString());

            sendPOST(addArtifactTemplate, params, mapper.valueToTree(art)).getOptHttpResponse();
        }

        public void finishAction(BrowserSessionId expeditionId, FinishStoryActionAddArtifactData art) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String>  params = ImmutableMap.of("expeditionId", expeditionId.getValueAsString());
            sendPOST(addArtifactTemplate, params, mapper.valueToTree(art)).getOptHttpResponse();
        }
    }

    /**
     * Creates a BrowserSessionClient based on a WebmateApiSession.
     *
     * @param session The WebmateApiSession the BrowserSessionClient is supposed to be based on
     */
    public BrowserSessionClient(WebmateAPISession session) {
        this.session = session;
        this.apiClient = new BrowserSessionApiClient(session.authInfo, session.environment);
    }

    /**
     * Creates a BrowserSessionClient based on a WebmateApiSession and a custom HttpClientBuilder.
     *
     * @param session The WebmateApiSession the BrowserSessionClient is supposed to be based on
     * @param httpClientBuilder The HttpClientBuilder that is used for building the underlying connection
     */
    public BrowserSessionClient(WebmateAPISession session,  HttpClientBuilder httpClientBuilder) {
        this.session = session;
        this.apiClient = new BrowserSessionApiClient(session.authInfo, session.environment, httpClientBuilder);
    }



    /**
     * Return the webmate BrowserSessionId for a given Selenium session running in webmate.
     *
     * @param opaqueSeleniumSessionIdString Selenium SessionId that can be obtained by calling WebDriver.getSessionId().toString().
     * @return BrowserSessionRef that can be used to interact with the BrowserSession
     */
    public BrowserSessionRef getBrowserSessionForSeleniumSession(String opaqueSeleniumSessionIdString) {
        // it seems that currently the BrowserSessionId is equal to the Selenium SessionId (which I would consider a bug)
        return new BrowserSessionRef(new BrowserSessionId(UUID.fromString(opaqueSeleniumSessionIdString)), session);
    }

    /**
     * Create a new State for the given BrowserSession with a default timeout of 5 minutes.
     *
     * @param browserSessionId BrowserSession, in which the state should be extracted.
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public void createState(BrowserSessionId browserSessionId, String matchingId) {
        createState(browserSessionId, matchingId, DefaultBrowserSessionTimeoutMillis);
    }

    /**
     * Create a new State for the given BrowserSession with a default timeout of 5 minutes.
     *
     * @param browserSessionId BrowserSession, in which the state should be extracted.
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @param browserSessionStateExtractionConfig configuration controlling the state extraction process. See {@link BrowserSessionScreenshotExtractionConfig}.
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public void createState(BrowserSessionId browserSessionId, String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
        createState(browserSessionId, matchingId, DefaultBrowserSessionTimeoutMillis, browserSessionStateExtractionConfig);
    }


    /**
     * Create a new State for the given BrowserSession.
     *
     * @param browserSessionId BrowserSession, in which the state should be extracted.
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @param timeoutMillis Maximal amount of time to wait for the state extraction to complete in milliseconds.
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public void createState(BrowserSessionId browserSessionId, String matchingId, long timeoutMillis) {
        LOG.debug("Creating state with matching id [" + matchingId + "] for browsersession [" + browserSessionId + "]");
        apiClient.createState(browserSessionId, matchingId, timeoutMillis);
    }

    /**
     * Create a new State for the given BrowserSession.
     *
     * @param browserSessionId BrowserSession, in which the state should be extracted.
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @param timeoutMillis Maximal amount of time to wait for the state extraction to complete in milliseconds.
     * @param browserSessionStateExtractionConfig configuration controlling the state extraction process. See {@link BrowserSessionStateExtractionConfig}.
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public void createState(BrowserSessionId browserSessionId, String matchingId, long timeoutMillis, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
        LOG.debug("Creating state with matching id [" + matchingId + "] for browsersession [" + browserSessionId + "]");
        apiClient.createState(browserSessionId, matchingId, timeoutMillis, browserSessionStateExtractionConfig);
    }

    /**
     * Create a new State for the BrowserSession registered in webmate session (there must be only one).
     *
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @param timeoutMillis Maximal amount of time to wait for the state extraction to complete in milliseconds.
     * @param browserSessionStateExtractionConfig configuration controlling the state extraction process. See {@link BrowserSessionStateExtractionConfig}.
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public void createState(String matchingId, long timeoutMillis, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
        List<BrowserSessionId> associatedExpeditions = session.getAssociatedExpeditions();
        if (associatedExpeditions.size() != 1) {
            throw new WebmateApiClientException("If createState is called without browsersession id, there must be only one " +
                    "BrowserSession associated with the API session (to be able to identify the correct one) " +
                    "but currently there are " + associatedExpeditions.size());
        }

        BrowserSessionId browserSessionId = associatedExpeditions.get(0);
        LOG.info("Creating state for browsersession " + browserSessionId);

        LOG.debug("Creating state with matching id [" + matchingId + "] for browsersession [" + browserSessionId + "]");
        apiClient.createState(browserSessionId, matchingId, timeoutMillis, browserSessionStateExtractionConfig);
    }

    /**
     * Create a new State for the BrowserSession registered in webmate session (there must be only one).
     *
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @param browserSessionStateExtractionConfig configuration controlling the state extraction process. See {@link BrowserSessionStateExtractionConfig}.
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public void createState(String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
        List<BrowserSessionId> associatedExpeditions = session.getAssociatedExpeditions();
        if (associatedExpeditions.size() != 1) {
            throw new WebmateApiClientException("If createState is called without browsersession id, there must be only one " +
                    "BrowserSession associated with the API session (to be able to identify the correct one) " +
                    "but currently there are " + associatedExpeditions.size());
        }

        BrowserSessionId browserSessionId = associatedExpeditions.get(0);
        LOG.info("Creating state for browsersession " + browserSessionId);

        LOG.debug("Creating state with matching id [" + matchingId + "] for browsersession [" + browserSessionId + "]");
        apiClient.createState(browserSessionId, matchingId, DefaultBrowserSessionTimeoutMillis, browserSessionStateExtractionConfig);
    }

    /**
     * Create a new State for the BrowserSession registered in webmate session (there must be only one). Uses defaults for timeout and
     * state extraction configuration.
     *
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public void createState(String matchingId) {
        List<BrowserSessionId> associatedExpeditions = session.getAssociatedExpeditions();
        if (associatedExpeditions.size() != 1) {
            throw new WebmateApiClientException("If createState is called without browsersession id, there must be only one " +
                    "BrowserSession associated with the API session (to be able to identify the correct one) " +
                    "but currently there are " + associatedExpeditions.size());
        }

        BrowserSessionId browserSessionId = associatedExpeditions.get(0);
        LOG.info("Creating state for browsersession " + browserSessionId);

        LOG.debug("Creating state with matching id [" + matchingId + "] for browsersession [" + browserSessionId + "]");
        apiClient.createState(browserSessionId, matchingId, DefaultBrowserSessionTimeoutMillis, DefaultStateExtractionConfig);
    }


    public void startAction(String actionName) {
        LOG.debug("Start action " + actionName);
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        ActionSpanId spanId = new ActionSpanId(UUID.randomUUID());
        StartStoryActionAddArtifactData artifactData = new StartStoryActionAddArtifactData(actionName, spanId);
        apiClient.startAction(expeditionId, artifactData);
        this.currentSpanIds.push(spanId);
        return;
    }

    public void finishAction() {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (this.currentSpanIds.isEmpty()) {
            throw new WebmateApiClientException("Trying to finish action but none is active.");
        }
        ActionSpanId spanId = this.currentSpanIds.pop();
        apiClient.finishAction(expeditionId, FinishStoryActionAddArtifactData.successful(spanId));
    }

    public void finishAction(String successMessage) {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (this.currentSpanIds.isEmpty()) {
            throw new WebmateApiClientException("Trying to finish action but none is active.");
        }
        ActionSpanId spanId = this.currentSpanIds.pop();
        apiClient.finishAction(expeditionId, FinishStoryActionAddArtifactData.successful(spanId, successMessage));
    }

    public void finishActionAsFailure(String errorMessage) {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (this.currentSpanIds.isEmpty()) {
           throw new WebmateApiClientException("Trying to finish action but none is active.");
        }
        ActionSpanId spanId = this.currentSpanIds.pop();
        apiClient.finishAction(expeditionId, FinishStoryActionAddArtifactData.failure(spanId, errorMessage, Optional.<JsonNode>absent()));
    }


    /**
     * Terminate the given BrowserSession
     *
     * @param browserSessionId The Id for the BrowserSession that is supposed to be terminated
     * @return true if the Browsersession was successfully terminated
     */
    public boolean terminateBrowsersession(BrowserSessionId browserSessionId) {
        LOG.debug("Trying to terminate Browsersession with id ["+ browserSessionId +"]");
        return apiClient.terminateSession(browserSessionId);
    }

}
