package com.testfabrik.webmate.javasdk.testmgmt;

import com.testfabrik.webmate.javasdk.WebmateAPISession;

/**
 * Facade for a (running or finished) TestRun
 */
public class TestRun {

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
     * Finish the test run.
     * This method is blocking:
     * it internally calls the {@link #waitForCompletion() waitForCompletion} method
     * to wait until the test run is finished.
     * If the test run does not finish before the timeout, the method will still return;
     * to detect this case, you have to check the test run manually.
     *
     * @param status The status to finish the test run with.
     * @param msg    A short message explaining the result of the test run.
     * @param detail Detailed information, e.g. a stack trace.
     */
    public void finish(TestRunEvaluationStatus status, String msg, String detail) {
        this.session.testMgmt.finishTestRun(id, status, msg, detail);
    }

    /**
     * Finish the test run.
     * This method is blocking:
     * it internally calls the {@link #waitForCompletion() waitForCompletion} method
     * to wait until the test run is finished.
     * If the test run does not finish before the timeout, the method will still return;
     * to detect this case, you have to check the test run manually.
     *
     * @param status The status to finish the test run with.
     * @param msg    A short message explaining the result of the test run.
     */
    public void finish(TestRunEvaluationStatus status, String msg) {
        this.session.testMgmt.finishTestRun(id, status, msg);
    }

    /**
     * Finish the test run.
     * This method is blocking:
     * it internally calls the {@link #waitForCompletion() waitForCompletion} method
     * to wait until the test run is finished.
     * If the test run does not finish before the timeout, the method will still return;
     * to detect this case, you have to check the test run manually.
     *
     * @param status The status to finish the test run with.
     */
    public void finish(TestRunEvaluationStatus status) {
        this.session.testMgmt.finishTestRun(id, status);
    }

    /**
     * Block until the test run goes into a finished state (completed or failed) or timeout occurs.
     * The default timeout is 10 minutes.
     * If the test run does not finish before the timeout, the method will still return;
     * to detect this case, you have to check the returned test run info manually.
     *
     * @return The test run info of the finished test run.
     */
    public TestRunInfo waitForCompletion() {
        return this.session.testMgmt.waitForTestRunCompletion(this.id);
    }

}
