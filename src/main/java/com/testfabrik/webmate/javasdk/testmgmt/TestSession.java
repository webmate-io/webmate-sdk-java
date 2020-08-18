package com.testfabrik.webmate.javasdk.testmgmt;

import com.testfabrik.webmate.javasdk.WebmateAPISession;

/**
 * Facade for a (running or finished) TestSession
 */
public class TestSession {

    private WebmateAPISession session;
    private TestSessionId id;

    TestSession(TestSessionId id, WebmateAPISession session) {
        this.id = id;
        this.session = session;
    }

    /**
     * Id of TestSession.
     */
    public TestSessionId getId() {
        return id;
    }
}
