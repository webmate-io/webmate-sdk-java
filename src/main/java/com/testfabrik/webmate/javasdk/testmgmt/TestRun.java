package com.testfabrik.webmate.javasdk.testmgmt;

import com.testfabrik.webmate.javasdk.WebmateAPISession;

/**
 * Facade for a (running or finished) TestRun
 */
public class TestRun {

    private WebmateAPISession session;
    private TestRunId id;

    TestRun(TestRunId id, WebmateAPISession session) {
        this.id = id;
        this.session = session;
    }

    /**
     * Finish TestRun.
     * @param msg Short message explaining the result of the test run.
     * @param detail Detailed information, e.g. stack trace.
     */
    public void finish(TestRunEvaluationStatus status, String msg, String detail) {
        this.session.testMgmt.finishTestRun(id, status, msg, detail);
    }

    /**
     * Finish TestRun.
     * @param msg Short message explaining the result of the test run.
     */
    public void finish(TestRunEvaluationStatus status, String msg) {
        this.session.testMgmt.finishTestRun(id, status, msg);
    }

    /**
     * Finish TestRun.
     */
    public void finish(TestRunEvaluationStatus status) {
        this.session.testMgmt.finishTestRun(id, status);
    }
}
