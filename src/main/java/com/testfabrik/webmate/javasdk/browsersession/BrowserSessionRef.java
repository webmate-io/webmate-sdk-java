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
     * @param matchingId The Id for the state. Used for matching.
     */
    public void createState(String matchingId) {
        session.browserSession.createState(browserSessionId, matchingId);
    }

    /**
     * Creates a State for a Browsersession with a matching id using a custom config.
     * @param matchingId The Id for the state. Used for matching.
     * @param browserSessionStateExtractionConfig The custom config that is supposed to be used.
     */
    public void createState(String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig) {
        session.browserSession.createState(browserSessionId, matchingId, browserSessionStateExtractionConfig);
    }

    /**
     * Creates a State for a Browsersession with a matching id using a custom config.
     * @param matchingId The Id for the state. Used for matching.
     * @param browserSessionStateExtractionConfig The custom config that is supposed to be used.
     * @param timeout The timeout for the state extraction in milliseconds.
     */
    public void createState(String matchingId, BrowserSessionStateExtractionConfig browserSessionStateExtractionConfig, long timeout) {
        session.browserSession.createState(browserSessionId, matchingId, timeout, browserSessionStateExtractionConfig);
    }

    /**
     * Creates a State for a Browsersession with a matching id using a custom config.
     * @param matchingId The Id for the state. Used for matching.
     * @param timeout The timeout for the state extraction in milliseconds.
     */
    public void createState(String matchingId, long timeout) {
        session.browserSession.createState(browserSessionId, matchingId, timeout);
    }

    /**
     * Start custom action with given name. If there is another action already active, this action will be a
     * child action of that one.
     */
    public void startAction(String actionName) {
        session.browserSession.startAction(actionName);
    }

    /**
     * Finish the currenty active custom action as a success.
     */
    public void finishAction() {
        session.browserSession.finishAction();
    }

    /**
     * Finish the currently active custom action successfully with message.
     */
    public void finishAction(String successMessage) {
        session.browserSession.finishAction(successMessage);
    }

    /**
     * Finish the currently active custom action and mark as failed with the given message.
     */
    public void finishActionAsFailure(String errorMessage) {
        session.browserSession.finishActionAsFailure(errorMessage);
    }

    /**
     * Terminates the BrowserSession
     * @return true if the BrowserSession was successfully terminated
     */
    public boolean terminateSession(){
        return session.browserSession.terminateBrowsersession(browserSessionId);
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
