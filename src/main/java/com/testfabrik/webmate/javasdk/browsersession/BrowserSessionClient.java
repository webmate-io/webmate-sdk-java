package com.testfabrik.webmate.javasdk.browsersession;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.testfabrik.webmate.javasdk.*;
import com.testfabrik.webmate.javasdk.commonutils.HttpHelpers;
import com.testfabrik.webmate.javasdk.utils.JsonUtils;
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

    private final Map<BrowserSessionId, Stack<ActionSpanId>> currentSpanIds = new HashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(BrowserSessionClient.class);

    // Sane default Config, extracting DOM and taking non fullpage Screenshots. Otherwise take Webmate Defaults.
    private static final BrowserSessionStateExtractionConfig DefaultStateExtractionConfig = new BrowserSessionStateExtractionConfig(null, null, null, null, null, true,
            new BrowserSessionScreenshotExtractionConfig(true, false), null);
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

        private final static UriTemplate retrieveBrowserSessionInfoTemplate =
                new UriTemplate("/browsersession/${browserSessionId}/info");


        /**
         * Creates an webmate api client.
         *
         * @param authInfo    The authentication information needed for the API interaction
         * @param environment The environment the client should be used in, i.e which urls to use for communication
         */
        public BrowserSessionApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment) {
            super(authInfo, environment);
        }


        /**
         * Creates an webmate api client using a custom HttpClientBuilder, which allows the use of proxies.
         *
         * @param authInfo          The authentication information needed for the API interaction
         * @param environment       The environment the client should be used in, i.e which urls to use for communication
         * @param httpClientBuilder The HttpClientBuilder that is used for the underlying connection.
         */
        public BrowserSessionApiClient(WebmateAuthInfo authInfo, WebmateEnvironment environment, HttpClientBuilder httpClientBuilder) {
            super(authInfo, environment, httpClientBuilder);
        }

        /**
         * Creates a State for a Browsersession with a matching id. The extraction parameters are set to default.
         *
         * @param browserSessionId The Browsersession Id for which the state should be associated with.
         * @param matchingId       The Id for the state. Used for matching.
         */
        public BrowserSessionStateId createState(BrowserSessionId browserSessionId, String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
            Map<String, Object> params = ImmutableMap.of("optMatchingId", matchingId, "extractionConfig", browserSessionStateExtractionConfig);
            JsonNode body = JsonUtils.getJsonFromData(params, JsonInclude.Include.NON_NULL);
            Optional<HttpResponse> optHttpResponse = sendPOST(createStateTemplate, ImmutableMap.of("browserSessionId", browserSessionId.toString()), body).getOptHttpResponse();
            return HttpHelpers.getObjectFromJsonEntity(optHttpResponse.get(), BrowserSessionStateId.class);
        }

        public void startAction(BrowserSessionId expeditionId, StartStoryActionAddArtifactData art) {
            Map<String, String> params = ImmutableMap.of("expeditionId", expeditionId.getValueAsString());
            sendPOST(addArtifactTemplate, params, JsonUtils.getJsonFromData(art)).getOptHttpResponse();
        }

        public void finishAction(BrowserSessionId expeditionId, FinishStoryActionAddArtifactData art) {
            Map<String, String> params = ImmutableMap.of("expeditionId", expeditionId.getValueAsString());
            sendPOST(addArtifactTemplate, params, JsonUtils.getJsonFromData(art)).getOptHttpResponse();
        }

        public BrowserSessionInfo getBrowserSessionInfo(BrowserSessionId id) {
            Optional<HttpResponse> r = sendGET(retrieveBrowserSessionInfoTemplate, ImmutableMap.of("browserSessionId", id.toString())).getOptHttpResponse();
            return HttpHelpers.getObjectFromJsonEntity(r.get(), BrowserSessionInfo.class);
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
     * @param session           The WebmateApiSession the BrowserSessionClient is supposed to be based on
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
     * @param matchingId                          Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @param browserSessionStateExtractionConfig configuration controlling the state extraction process. See {@link BrowserSessionStateExtractionConfig}.
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public BrowserSessionStateId createState(String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
        BrowserSessionId browserSessionId = session.getOnlyAssociatedExpedition();
        if (browserSessionId == null) {
            throw new WebmateApiClientException("If createState is called without browsersession id, there must be only one " +
                    "BrowserSession associated with the API session (to be able to identify the correct one)");
        }
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
        BrowserSessionId browserSessionId = session.getOnlyAssociatedExpedition();
        if (browserSessionId == null) {
            throw new WebmateApiClientException("If createState is called without browsersession id, there must be only one " +
                    "BrowserSession associated with the API session (to be able to identify the correct one)");
        }
        LOG.info("Creating state for browsersession " + browserSessionId);
        return createState(browserSessionId, matchingId);
    }

    /**
     * Create a new State for the given BrowserSession.
     *
     * @param browserSessionId BrowserSession, in which the state should be extracted.
     * @param matchingId       Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public BrowserSessionStateId createState(BrowserSessionId browserSessionId, String matchingId) {
        return createState(browserSessionId, matchingId, DefaultStateExtractionConfig);
    }

    /**
     * Create a new State for the given BrowserSession.
     *
     * @param browserSessionId                    BrowserSession, in which the state should be extracted.
     * @param matchingId                          Label for state (should be unique for BrowserSession, otherwise some tests could get confused).
     * @param browserSessionStateExtractionConfig configuration controlling the state extraction process. See {@link BrowserSessionStateExtractionConfig}.
     * @throws WebmateApiClientException if an error occurs while requesting state extraction or if the timeout is exceeded.
     */
    public BrowserSessionStateId createState(BrowserSessionId browserSessionId, String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
        LOG.debug("Creating state with matching id [" + matchingId + "] for browsersession [" + browserSessionId + "]");
        return apiClient.createState(browserSessionId, matchingId, browserSessionStateExtractionConfig);
    }

    /**
     * Wrap the given action lambda function in an action of the given expedition with the given name.
     * The action can be closed explicitly with the ActionDelegate argument provided to the lambda.
     * It also implicitly finishes as successful or an error if an exception is thrown within the lambda.
     * @return The return value of the lambda function, null on failure.
     */
    public <T> T withAction(BrowserSessionId expeditionId, String actionName, ActionFunc<T> actionFunc) {
        this.startAction(expeditionId, actionName);
        ActionDelegate actionDelegate = new ActionDelegate(this);
        T result = null;
        try {
            result = actionFunc.op(actionDelegate);
        } catch (Throwable e) {
            this.finishActionAsFailure(expeditionId, "Exception in '" + actionName + "': " + e.getMessage());
        } finally {
            this.finishAction(expeditionId);
        }
        return result;
    }

    /**
     * Wrap the given action lambda function in an action with the given name.
     * The expedition of the action is supplied using {@link WebmateAPISession#getOnlyAssociatedExpedition}.
     * If it returns null, the action lambda function is still executed, but not wrapped in an action.
     * The action can be closed explicitly with the ActionDelegate argument provided to the lambda.
     * It also implicitly finishes as successful or an error if an exception is thrown within the lambda.
     * @return The return value of the lambda function, null on failure.
     */
    public <T> T withAction(String actionName, ActionFunc<T> actionFunc) {
        this.startAction(actionName);
        ActionDelegate actionDelegate = new ActionDelegate(this);
        T result = null;
        try {
            result = actionFunc.op(actionDelegate);
        } catch (Throwable e) {
            this.finishActionAsFailure("Exception in '" + actionName + "': " + e.getMessage());
        } finally {
            this.finishAction();
        }
        return result;
    }

    /**
     * Wrap the given action lambda function in an action of the given expedition with the given name.
     * The action can be closed explicitly with the ActionDelegate argument provided to the lambda.
     * It also implicitly finishes as successful or an error if an exception is thrown within the lambda.
     */
    public void withAction(BrowserSessionId expeditionId, String actionName, ActionFuncVoid actionFunc) {
        this.startAction(expeditionId, actionName);
        ActionDelegate actionDelegate = new ActionDelegate(this);
        try {
            actionFunc.op(actionDelegate);
        } catch (Throwable e) {
            this.finishActionAsFailure(expeditionId, "Exception in '" + actionName + "': " + e.getMessage());
        } finally {
            this.finishAction(expeditionId);
        }
    }

    /**
     * Wrap the given action lambda function in an action with the given name.
     * The expedition of the action is supplied using {@link WebmateAPISession#getOnlyAssociatedExpedition}.
     * If it returns null, the action lambda function is still executed, but not wrapped in an action.
     * The action can be closed explicitly with the ActionDelegate argument provided to the lambda.
     * It also implicitly finishes as successful or an error if an exception is thrown within the lambda.
     */
    public void withAction(String actionName, ActionFuncVoid actionFunc) {
        this.startAction(actionName);
        ActionDelegate actionDelegate = new ActionDelegate(this);
        try {
            actionFunc.op(actionDelegate);
        } catch (Throwable e) {
            this.finishActionAsFailure("Exception in '" + actionName + "': " + e.getMessage());
        } finally {
            this.finishAction();
        }
    }

    /**
     * Start an action for the given expedition with the given name.
     */
    public void startAction(BrowserSessionId expeditionId, String actionName) {
        LOG.debug("Start action " + actionName);
        ActionSpanId spanId = new ActionSpanId(UUID.randomUUID());
        StartStoryActionAddArtifactData artifactData = new StartStoryActionAddArtifactData(actionName, spanId);
        apiClient.startAction(expeditionId, artifactData);
        if (!currentSpanIds.containsKey(expeditionId)) {
            currentSpanIds.put(expeditionId, new Stack<>());
        }
        currentSpanIds.get(expeditionId).push(spanId);
    }

    /**
     * Start an action with the given name.
     * The expedition of the action is supplied using {@link WebmateAPISession#getOnlyAssociatedExpedition}.
     * If it returns null, log a warning and do nothing.
     */
    public void startAction(String actionName) {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (expeditionId == null) {
            LOG.warn("Could not start action " + actionName);
            return;
        }
        startAction(expeditionId, actionName);
    }

    /**
     * Finish the newest action of the given expedition.
     * If there is no active action, log a warning and do nothing.
     */
    public void finishAction(BrowserSessionId expeditionId) {
        if (!currentSpanIds.containsKey(expeditionId) || currentSpanIds.get(expeditionId).isEmpty()) {
            LOG.warn("Trying to finish action but none is active.");
            return;
        }
        ActionSpanId spanId = currentSpanIds.get(expeditionId).pop();
        apiClient.finishAction(expeditionId, FinishStoryActionAddArtifactData.successful(spanId));
    }

    /**
     * Finish the newest action of the given expedition with the given message.
     * If there is no active action, log a warning and do nothing.
     */
    public void finishAction(BrowserSessionId expeditionId, String successMessage) {
        if (!currentSpanIds.containsKey(expeditionId) || currentSpanIds.get(expeditionId).isEmpty()) {
            LOG.warn("Trying to finish action but none is active.");
            return;
        }
        ActionSpanId spanId = currentSpanIds.get(expeditionId).pop();
        apiClient.finishAction(expeditionId, FinishStoryActionAddArtifactData.successful(spanId, successMessage));
    }

    /**
     * Fail the newest action of the given expedition with the given message.
     * If there is no active action, log a warning and do nothing.
     */
    public void finishActionAsFailure(BrowserSessionId expeditionId, String errorMessage) {
        if (!currentSpanIds.containsKey(expeditionId) || currentSpanIds.get(expeditionId).isEmpty()) {
            LOG.warn("Trying to finish action but none is active.");
            return;
        }
        ActionSpanId spanId = currentSpanIds.get(expeditionId).pop();
        apiClient.finishAction(expeditionId, FinishStoryActionAddArtifactData.failure(spanId, errorMessage, Optional.absent()));
    }

    /**
     * Finish the newest action of the newest expedition with the given message.
     * The expedition of the action is supplied using {@link WebmateAPISession#getOnlyAssociatedExpedition}.
     * If it returns null, or if there is no active action, log a warning and do nothing.
     */
    public void finishAction(String successMessage) {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (expeditionId == null) {
            LOG.warn("Could not finish action");
            return;
        }
        finishAction(expeditionId, successMessage);
    }

    /**
     * Finish the newest action of the newest expedition.
     * The expedition of the action is supplied using {@link WebmateAPISession#getOnlyAssociatedExpedition}.
     * If it returns null, or if there is no active action, log a warning and do nothing.
     */
    public void finishAction() {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (expeditionId == null) {
            LOG.warn("Could not finish action");
            return;
        }
        finishAction(expeditionId);
    }

    /**
     * @deprecated This method is deprecated.
     * Use {@link BrowserSessionClient#finishAction() finishAction} instead.
     */
    public void finishActionAsSuccessIgnoreNoneActive() {
        finishAction();
    }

    /**
     * @deprecated This method is deprecated.
     * Use {@link BrowserSessionClient#finishActionAsFailure(String) finishActionAsFailure} instead.
     */
    public void finishActionAsFailureIgnoreNoneActive(String errorMessage) {
        finishActionAsFailure(errorMessage);
    }

    /**
     * Fail the newest action of the newest expedition with the given message.
     * The expedition of the action is supplied using {@link WebmateAPISession#getOnlyAssociatedExpedition}.
     * If it returns null, or if there is no active action, log a warning and do nothing.
     */
    public void finishActionAsFailure(String errorMessage) {
        BrowserSessionId expeditionId = session.getOnlyAssociatedExpedition();
        if (expeditionId == null) {
            LOG.warn("Could not finish action");
            return;
        }
        finishActionAsFailure(expeditionId, errorMessage);
    }

    /**
     * @return False (because no browser session is being terminated successfully).
     * @deprecated This method is deprecated.
     * It is no longer possible to manually terminate browser sessions.
     */
    public boolean terminateBrowsersession(BrowserSessionId browserSessionId) {
        LOG.warn("Deprecated method terminateBrowsersession used: it is no longer possible to manually terminate browser sessions");
        return false;
    }

    /**
     * Retrieves info for this BrowserSession
     *
     * @param id The id of the BrowserSession that info should be retrieved for
     * @return BrowserSessionInfo for this BrowserSession
     */
    public BrowserSessionInfo getBrowserSessionInfo(BrowserSessionId id) {
        return apiClient.getBrowserSessionInfo(id);
    }

}
