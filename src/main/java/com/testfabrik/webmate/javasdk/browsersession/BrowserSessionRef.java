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

    public void createState(String matchingId) {
        session.browserSession.createState(browserSessionId, matchingId);
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
