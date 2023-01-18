package com.testfabrik.webmate.javasdk.testmgmt;

import com.testfabrik.webmate.javasdk.WebmateAPISession;

/**
 * Facade for a (running or finished) TestRun
 */
public class TestRun {

    private static final long MAX_WAITING_TIME_MILLIS = 300_000; // 300 seconds
    private static final long WAITING_POLLINTERVAL_MILLIS = 3_000; // 3 seconds

    private WebmateAPISession session;

    private TestRunId id;

    public TestRun(TestRunId id, WebmateAPISession session) {
        this.id = id;
        this.session = session;
    }

    public TestRunId getId() {
        return id;
    }

    /**
     * Retrieve current information about this test run from webmate.
     */
    public TestRunInfo retrieveCurrentInfo() {
        return this.session.testMgmt.getTestRun(id);
    }

    /**
     * Set the name for a given test run.
     *
     * @param name New TestRun name.
     */
    public void setName(String name) {
        this.session.testMgmt.setTestRunName(id, name);
    }

    /**
     * Finish TestRun.
     *
     * @param msg Short message explaining the result of the test run.
     * @param detail Detailed information, e.g. stack trace.
     */
    public void finish(TestRunEvaluationStatus status, String msg, String detail) {
        this.session.testMgmt.finishTestRun(id, status, msg, detail);
    }

    /**
     * Finish TestRun.
     *
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

    /**
     * Block, until the TestRun goes into a finished state (completed or failed).
     *
     * @return returns the TestRun info of the finished TestRun.
     */
    public TestRunInfo waitForCompletion() {
        long startTime = System.currentTimeMillis();
        TestRunInfo info = null;
        try {
            do {
                Thread.sleep(WAITING_POLLINTERVAL_MILLIS);
                info = retrieveCurrentInfo();
            } while ((info.getExecutionStatus() == TestRunExecutionStatus.RUNNING ||
                    info.getExecutionStatus() == TestRunExecutionStatus.CREATED ||
                    info.getExecutionStatus() == TestRunExecutionStatus.PENDING_PASSED ||
                    info.getExecutionStatus() == TestRunExecutionStatus.PENDING_FAILED) &&
                    System.currentTimeMillis() - startTime < MAX_WAITING_TIME_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return info;
    }

}
