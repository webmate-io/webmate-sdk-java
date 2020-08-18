package com.testfabrik.webmate.javasdk.testmgmt;

import com.testfabrik.webmate.javasdk.WebmateAPISession;
import com.testfabrik.webmate.javasdk.WebmateApiClient;

/**
 * Facade for a (running or finished) TestExecution
 */
public class TestExecution {

    private WebmateAPISession session;

    private TestExecution(WebmateAPISession session) {
        this.session = session;
    }
}
