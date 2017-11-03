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
