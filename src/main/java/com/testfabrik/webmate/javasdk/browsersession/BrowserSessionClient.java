package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import com.testfabrik.webmate.javasdk.commonutils.HttpHelpers;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Facade to webmate's BrowserSession subsystem.
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
    public interface ActionFunc<T> {
        T op(ActionDelegate action);
    }

    public interface ActionFuncVoid {
        void op(ActionDelegate action);
    }

    public static class ActionDelegate {

        private final BrowserSessionClient client;

        public ActionDelegate(BrowserSessionClient client) {
            this.client = client;
        }

        /**
         * Finish current action successfully.
         */
        public void finishAction() {
            this.client.finishAction();
        }

        /**
         * Finish current action as failure.
         */
        public void finishActionAsFailure(String errorMessage) {
            this.client.finishActionAsFailure(errorMessage);
        }
    }

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
        public BrowserSessionApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder httpClientBuilder) {
            super(authInfo, environment, httpClientBuilder);
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
         * Creates a State for a Browsersession with a matching id. The extraction parameters are set to default.
         *
         * @param browserSessionId The Browsersession Id for which the state should be associated with.
         * @param matchingId The Id for the state. Used for matching.
         */
        public BrowserSessionStateId createState(BrowserSessionId browserSessionId, String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
            Map<String, Object>  params = ImmutableMap.of("optMatchingId", matchingId, "extractionConfig", browserSessionStateExtractionConfig);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Optional<HttpResponse> optHttpResponse = sendPOST(createStateTemplate, ImmutableMap.of("browserSessionId", browserSessionId.toString()), mapper.valueToTree(params)).getOptHttpResponse();
            return HttpHelpers.getObjectFromJsonEntity(optHttpResponse.get(), BrowserSessionStateId.class);
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
    public BrowserSessionClient(WebmateAPISession session, HttpClientBuilder httpClientBuilder) {
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
     * Create a new State for the BrowserSession registered in webmate session (there must be only one).
     *
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @param browserSessionStateExtractionConfig configuration controlling the state extraction process. See {@link BrowserSessionStateExtractionConfig}.
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public BrowserSessionStateId createState(String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
        List<BrowserSessionId> associatedExpeditions = session.getAssociatedExpeditions();
        if (associatedExpeditions.size() != 1) {
            throw new WebmateApiClientException("If createState is called without browsersession id, there must be only one " +
                    "BrowserSession associated with the API session (to be able to identify the correct one) " +
                    "but currently there are " + associatedExpeditions.size());
        }

        BrowserSessionId browserSessionId = associatedExpeditions.get(0);
        LOG.info("Creating state for browsersession " + browserSessionId);

        return createState(browserSessionId, matchingId, browserSessionStateExtractionConfig);
    }

    /**
     * Create a new State for the BrowserSession registered in webmate session (there must be only one). Uses defaults for timeout and
     * state extraction configuration.
     *
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public BrowserSessionStateId createState(String matchingId) {
        List<BrowserSessionId> associatedExpeditions = session.getAssociatedExpeditions();
        if (associatedExpeditions.size() != 1) {
            throw new WebmateApiClientException("If createState is called without browsersession id, there must be only one " +
                    "BrowserSession associated with the API session (to be able to identify the correct one) " +
                    "but currently there are " + associatedExpeditions.size());
        }

        BrowserSessionId browserSessionId = associatedExpeditions.get(0);
        LOG.info("Creating state for browsersession " + browserSessionId);

        return createState(browserSessionId, matchingId);
    }

    /**
     * Create a new State for the given BrowserSession.
     *
     * @param browserSessionId BrowserSession, in which the state should be extracted.
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public BrowserSessionStateId createState(BrowserSessionId browserSessionId, String matchingId) {
        return createState(browserSessionId, matchingId, DefaultStateExtractionConfig);
    }

    /**
     * Create a new State for the given BrowserSession.
     *
     * @param browserSessionId BrowserSession, in which the state should be extracted.
     * @param matchingId Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @param browserSessionStateExtractionConfig configuration controlling the state extraction process. See {@link BrowserSessionStateExtractionConfig}.
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public BrowserSessionStateId createState(BrowserSessionId browserSessionId, String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
        LOG.debug("Creating state with matching id [" + matchingId + "] for browsersession [" + browserSessionId + "]");
        return apiClient.createState(browserSessionId, matchingId, browserSessionStateExtractionConfig);
    }

    /**
     * Wrap the given action (lambda) in an Action with the given name. The action can be
     * closed explicitly with the ActionDelegate argument provided to the lambda. It also
     * implicitly finishes as successful or an error if an exception is throws within the lambda.
     *
     * @param actionName Name of action
     * @param actionFunc function executed within action.
     * @param <T> Return value of inner code.
     * @return Returned value of lambda
     */
    public <T> T withAction(String actionName, ActionFunc<T> actionFunc) {
        this.startAction(actionName);
        ActionDelegate actionDelegate = new ActionDelegate(this);
        T result = null;
        try {
            result = actionFunc.op(actionDelegate);
        } catch (Throwable e) {
            this.finishActionAsFailureIgnoreNoneActive("Exception in '" + actionName + "': " + e.getMessage());
        } finally {
            this.finishActionAsSuccessIgnoreNoneActive();
        }
        return result;
    }

    /**
     * Wrap the given action (lambda) in an Action with the given name. The action can be
     * closed explicitly with the ActionDelegate argument provided to the lambda. It also
     * implicitly finishes as successful or an error if an exception is throws within the lambda.
     *
     * @param actionName Name of action
     * @param actionFunc function executed within action.
     */
    public void withAction(String actionName, ActionFuncVoid actionFunc) {
        this.startAction(actionName);
        ActionDelegate actionDelegate = new ActionDelegate(this);
        try {
            actionFunc.op(actionDelegate);
        } catch (Throwable e) {
            this.finishActionAsFailureIgnoreNoneActive("Exception in '" + actionName + "': " + e.getMessage());
        } finally {
            this.finishActionAsSuccessIgnoreNoneActive();
        }
    }

    /**
     * Start action with the given name.
     */
    public void startAction(String actionName) {
        LOG.debug("Start action " + actionName);
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        ActionSpanId spanId = new ActionSpanId(UUID.randomUUID());
        StartStoryActionAddArtifactData artifactData = new StartStoryActionAddArtifactData(actionName, spanId);
        apiClient.startAction(expeditionId, artifactData);
        this.currentSpanIds.push(spanId);
    }

    /**
     * Finish the currently active Action and provide a success message.
     *
     * @param successMessage message that should be added to the action
     * @throws WebmateApiClientException if no action is active
     */
    public void finishAction(String successMessage) {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (this.currentSpanIds.isEmpty()) {
            throw new WebmateApiClientException("Trying to finish action but none is active.");
        }
        ActionSpanId spanId = this.currentSpanIds.pop();
        apiClient.finishAction(expeditionId, FinishStoryActionAddArtifactData.successful(spanId, successMessage));
    }

    public void finishAction() {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (this.currentSpanIds.isEmpty()) {
            throw new WebmateApiClientException("Trying to finish action but none is active.");
        }
        ActionSpanId spanId = this.currentSpanIds.pop();
        apiClient.finishAction(expeditionId, FinishStoryActionAddArtifactData.successful(spanId));
    }

    /**
     * Finish the currently active Action. Do nothing if there is no active action.
     */
    public void finishActionAsSuccessIgnoreNoneActive() {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (this.currentSpanIds.isEmpty()) {
            // we don't care
            return;
        }
        ActionSpanId spanId = this.currentSpanIds.pop();
        apiClient.finishAction(expeditionId, FinishStoryActionAddArtifactData.successful(spanId));
    }

    /**
     * Finish the currently active Action and mark is as failed. Do nothing if there is no active action.
     *
     * @param errorMessage Error message indicating why this action has failed.
     */
    public void finishActionAsFailureIgnoreNoneActive(String errorMessage) {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (this.currentSpanIds.isEmpty()) {
            // we don't care
            return;
        }
        ActionSpanId spanId = this.currentSpanIds.pop();
        apiClient.finishAction(expeditionId, FinishStoryActionAddArtifactData.failure(spanId, errorMessage, Optional.<JsonNode>absent()));
    }

    /**
     * Finish the currently active Action and mark it as failure.
     *
     * @param errorMessage Error message indicating why this action has failed.
     * @throws WebmateApiClientException if no action is active
     */
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
