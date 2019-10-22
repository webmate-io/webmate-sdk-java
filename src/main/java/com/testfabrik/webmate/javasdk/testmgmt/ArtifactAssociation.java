package com.testfabrik.webmate.javasdk.testmgmt;

import com.google.common.base.Optional;
import com.testfabrik.webmate.javasdk.browsersession.BrowserSessionId;

public class ArtifactAssociation {

    private BrowserSessionId browserSession;
    private TestRunId testRun;


    //for jackson
    private ArtifactAssociation(){}

    private ArtifactAssociation(BrowserSessionId browserSession, TestRunId associatedTestRun) {
        this.browserSession = browserSession;
        this.testRun = associatedTestRun;
    }


    public BrowserSessionId getBrowserSession() {
        return browserSession;
    }

    public void setBrowserSession(BrowserSessionId browserSession) {
        this.browserSession = browserSession;
    }

    public TestRunId getTestRun() {
        return testRun;
    }

    public void setTestRun(TestRunId testRun) {
        this.testRun = testRun;
    }
}
