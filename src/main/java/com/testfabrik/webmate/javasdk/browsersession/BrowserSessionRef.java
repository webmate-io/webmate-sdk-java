package com.testfabrik.webmate.javasdk.browsersession;

import com.testfabrik.webmate.javasdk.WebmateAPISession;

import java.util.Objects;

/**
 * Convenience handle object wrapping the webmate session with a BrowserSessionId.
 */
public class BrowserSessionRef {

    public final BrowserSessionId browserSessionId;

    private final WebmateAPISession session;

    public BrowserSessionRef(BrowserSessionId browserSessionId, WebmateAPISession session) {
        this.browserSessionId = browserSessionId;
        this.session = session;
    }

    /**
     * Creates a State for a Browsersession with a matching id. The extraction parameters are set to default.
     *
     * @param matchingId The Id for the state. Used for matching.
     */
    public BrowserSessionStateId createState(String matchingId) {
        return session.browserSession.createState(browserSessionId, matchingId);
    }

    /**
     * Creates a State for a Browsersession with a matching id using a custom config.
     *
     * @param matchingId The Id for the state. Used for matching.
     * @param browserSessionStateExtractionConfig The custom config that is supposed to be used.
     */
    public BrowserSessionStateId createState(String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
        return session.browserSession.createState(browserSessionId, matchingId, browserSessionStateExtractionConfig);
    }

    /**
     * Start a custom action with the given name.
     * If there is another action already active, this action will be a child action of that one.
     */
    public void startAction(String actionName) {
        session.browserSession.startAction(browserSessionId, actionName);
    }

    /**
     * Wrap the given action lambda function in a custom action with the given name.
     * The action can be closed explicitly with the ActionDelegate argument provided to the lambda.
     * It also implicitly finishes as successful or an error if an exception is thrown within the lambda.
     * @return The return value of the lambda function, null on failure.
     */
    public <T> T withAction(String actionName, BrowserSessionClient.ActionFunc<T> actionFunc) {
        return session.browserSession.withAction(browserSessionId, actionName, actionFunc);
    }

    /**
     * Wrap the given action lambda function in a custom action with the given name.
     * The action can be closed explicitly with the ActionDelegate argument provided to the lambda.
     * It also implicitly finishes as successful or an error if an exception is thrown within the lambda.
     */
    public void withAction(String actionName, BrowserSessionClient.ActionFuncVoid actionFunc) {
        session.browserSession.withAction(browserSessionId, actionName, actionFunc);
    }

    /**
     * Finish the newest custom action.
     * If there is no active action, log a warning and do nothing.
     */
    public void finishAction() {
        session.browserSession.finishAction(browserSessionId);
    }

    /**
     * Finish the newest custom action with the given message.
     * If there is no active action, log a warning and do nothing.
     */
    public void finishAction(String successMessage) {
        session.browserSession.finishAction(browserSessionId, successMessage);
    }

    /**
     * Fail the newest custom action with the given message.
     * If there is no active action, log a warning and do nothing.
     */
    public void finishActionAsFailure(String errorMessage) {
        session.browserSession.finishActionAsFailure(browserSessionId, errorMessage);
    }

    /**
     * @deprecated
     * This method is deprecated.
     * It is no longer possible to manually terminate browser sessions.
     * @return False (because no browser session is being terminated successfully).
     */
    public boolean terminateSession() {
        return session.browserSession.terminateBrowsersession(browserSessionId);
    }

    /**
     * Retrieves info for this BrowserSession
     * @return BrowserSessionInfo for this BrowserSession
     */
    public BrowserSessionInfo getBrowserSessionInfo() {
        return session.browserSession.getBrowserSessionInfo(browserSessionId);
    }


    @Override
    public int hashCode() {
        return Objects.hash(browserSessionId, session);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        return obj instanceof BrowserSessionRef
                && Objects.equals(browserSessionId, ((BrowserSessionRef) obj).browserSessionId)
                && Objects.equals(session, ((BrowserSessionRef) obj).session);
    }

    @Override
    public String toString() {
        return browserSessionId.toString();
    }
}
